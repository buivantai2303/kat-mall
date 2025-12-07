/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.usecase;

import com.en.katmall.co.identity.application.dto.response.VerificationResponse;
import com.en.katmall.co.identity.domain.model.MemberRegistrationModel;
import com.en.katmall.co.identity.domain.model.UserModel;
import com.en.katmall.co.identity.domain.model.valueobject.Email;
import com.en.katmall.co.identity.domain.repository.MemberRegistrationRepository;
import com.en.katmall.co.identity.domain.repository.UserRepository;
import com.en.katmall.co.shared.enums.KTypeIdentifier;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.infrastructure.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Verify Registration
 * Verifies email/phone token and creates actual user account.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VerifyRegistrationUseCase {

    private final MemberRegistrationRepository memberRegistrationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    /**
     * Executes the verification use case
     * 
     * @param token Verification token
     * @return Verification response
     */
    public VerificationResponse execute(String token) {
        Objects.requireNonNull(token, "Token must not be null");

        // Find registration by token
        MemberRegistrationModel registration = memberRegistrationRepository.findByVerificationToken(token)
                .orElseThrow(() -> new DomainException("TOKEN_INVALID", "error.registration.token.invalid"));

        // Verify (checks expiration)
        registration.verify();

        // Create user from registration
        UserModel userModel = createUserFromRegistration(registration);
        UserModel savedUserModel = userRepository.save(userModel);

        // Delete member registration
        memberRegistrationRepository.deleteById(registration.getId());

        // Send welcome email
        if (registration.getIdentifierType() == KTypeIdentifier.EMAIL) {
            emailService.sendWelcomeEmail(registration.getIdentifier(), null);
        }

        log.info("Verified registration for user: {}", savedUserModel.getId());

        return VerificationResponse.builder()
                .userId(savedUserModel.getId())
                .email(registration.getEmail())
                .phone(registration.getPhone())
                .message("Account verified successfully. You can now login.")
                .verified(true)
                .build();
    }

    private UserModel createUserFromRegistration(MemberRegistrationModel registration) {
        if (registration.getIdentifierType() == KTypeIdentifier.EMAIL) {
            Email email = Email.of(registration.getIdentifier());
            UserModel userModel = UserModel.create("New User", email, registration.getPasswordHash());
            userModel.verifyEmail();
            return userModel;
        } else {
            // Phone registration - create with temporary email
            Email tempEmail = Email.of(registration.getIdentifier() + "@phone.katmall.vn");
            UserModel userModel = UserModel.create("New User", tempEmail, registration.getPasswordHash());
            userModel.updatePhone(registration.getIdentifier());
            return userModel;
        }
    }
}
