/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.usecase;

import com.en.katmall.co.identity.application.dto.request.QuickRegisterRequest;
import com.en.katmall.co.identity.application.dto.response.PendingRegistrationResponse;
import com.en.katmall.co.identity.domain.model.UserRegistrationModel;
import com.en.katmall.co.identity.domain.model.valueobject.Email;
import com.en.katmall.co.identity.domain.repository.UserRegistrationRepository;
import com.en.katmall.co.identity.domain.repository.UserRepository;
import com.en.katmall.co.identity.domain.service.PasswordValidator;
import com.en.katmall.co.identity.infrastructure.security.BcryptPasswordEncoder;
import com.en.katmall.co.shared.enums.KTypeIdentifier;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.infrastructure.config.properties.RegistrationProperties;
import com.en.katmall.co.shared.infrastructure.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Register New User
 * Creates a pending registration and sends verification email/SMS.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RegisterUseCase {

    private final UserRegistrationRepository userRegistrationRepository;
    private final UserRepository userRepository;
    private final BcryptPasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final EmailService emailService;
    private final RegistrationProperties registrationProperties;

    /**
     * Executes the registration use case
     * 
     * @param request Registration request
     * @return Pending registration response
     */
    public PendingRegistrationResponse execute(QuickRegisterRequest request) {
        Objects.requireNonNull(request, "Request must not be null");

        // Validate password
        passwordValidator.validateWithConfirmation(request.getPassword(), request.getConfirmPassword());

        // Detect identifier type
        String identifier = request.getIdentifier().trim();
        KTypeIdentifier identifierType = KTypeIdentifier.detect(identifier);

        // Normalize phone if needed
        if (identifierType == KTypeIdentifier.PHONE) {
            identifier = KTypeIdentifier.normalizePhone(identifier);
        }

        // Check if already registered
        checkNotAlreadyRegistered(identifier, identifierType);

        // Check if pending registration exists
        checkAndHandlePendingRegistration(identifier);

        // Hash password
        String passwordHash = passwordEncoder.encode(request.getPassword());

        // Create member registration
        UserRegistrationModel registration = UserRegistrationModel.builder()
                .identifier(identifier)
                .identifierType(identifierType)
                .passwordHash(passwordHash)
                .expirationHours(registrationProperties.getTokenExpirationHours())
                .build();

        UserRegistrationModel saved = userRegistrationRepository.save(registration);

        // Send verification email/SMS
        sendVerification(saved);

        log.info("Created member registration for: {} ({})", maskIdentifier(identifier), identifierType);

        return PendingRegistrationResponse.builder()
                .pendingId(saved.getId())
                .maskedIdentifier(maskIdentifier(identifier))
                .expiresAt(saved.getExpiresAt())
                .remainingResendAttempts(saved.getRemainingResendAttempts())
                .message("Verification email sent. Please check your inbox.")
                .build();
    }

    private void checkNotAlreadyRegistered(String identifier, KTypeIdentifier type) {
        boolean exists;
        if (type == KTypeIdentifier.EMAIL) {
            Email email = Email.of(identifier);
            exists = userRepository.existsByEmail(email);
        } else {
            exists = userRepository.existsByPhone(identifier);
        }

        if (exists) {
            throw new DomainException("IDENTIFIER_ALREADY_REGISTERED", "error.registration.identifier.exists");
        }
    }

    private void checkAndHandlePendingRegistration(String identifier) {
        userRegistrationRepository.findByIdentifier(identifier).ifPresent(existing -> {
            if (existing.isExpired()) {
                userRegistrationRepository.deleteById(existing.getId());
            } else {
                throw new DomainException("IDENTIFIER_PENDING_VERIFICATION",
                        "error.registration.pending.verification");
            }
        });
    }

    private void sendVerification(UserRegistrationModel registration) {
        String verifyUrl = registrationProperties.generateVerifyUrl(registration.getVerificationToken());

        if (registration.getIdentifierType() == KTypeIdentifier.EMAIL) {
            emailService.sendVerificationEmail(
                    registration.getIdentifier(),
                    verifyUrl,
                    registrationProperties.getTokenExpirationHours());
        } else {
            // TODO: Implement SMS service
            log.warn("SMS verification not implemented yet. Token: {}", registration.getVerificationToken());
        }
    }

    private String maskIdentifier(String identifier) {
        if (identifier.contains("@")) {
            int atIndex = identifier.indexOf("@");
            if (atIndex <= 2) {
                return identifier.charAt(0) + "***" + identifier.substring(atIndex);
            }
            return identifier.substring(0, 2) + "***" + identifier.substring(atIndex);
        } else {
            if (identifier.length() <= 4)
                return "***";
            return identifier.substring(0, 3) + "***" + identifier.substring(identifier.length() - 4);
        }
    }
}
