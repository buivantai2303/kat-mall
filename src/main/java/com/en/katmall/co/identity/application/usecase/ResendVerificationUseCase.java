/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.usecase;

import com.en.katmall.co.identity.application.dto.request.ResendVerificationRequest;
import com.en.katmall.co.identity.application.dto.response.PendingRegistrationResponse;
import com.en.katmall.co.identity.domain.model.UserRegistrationModel;
import com.en.katmall.co.identity.domain.repository.UserRegistrationRepository;
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
 * Use Case: Resend Verification
 * Resends verification email/SMS for pending registration.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ResendVerificationUseCase {

    private final UserRegistrationRepository userRegistrationRepository;
    private final EmailService emailService;
    private final RegistrationProperties registrationProperties;

    /**
     * Executes the resend verification use case
     * 
     * @param request Resend request
     * @return Updated pending registration response
     */
    public PendingRegistrationResponse execute(ResendVerificationRequest request) {
        Objects.requireNonNull(request, "Request must not be null");

        String identifier = request.getIdentifier().trim();
        KTypeIdentifier identifierType = KTypeIdentifier.detect(identifier);

        if (identifierType == KTypeIdentifier.PHONE) {
            identifier = KTypeIdentifier.normalizePhone(identifier);
        }

        // Find registration
        UserRegistrationModel registration = userRegistrationRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new DomainException("NOT_FOUND", "error.registration.not.found"));

        // Regenerate token
        registration.regenerateToken(registrationProperties.getTokenExpirationHours());
        UserRegistrationModel saved = userRegistrationRepository.save(registration);

        // Resend verification
        sendVerification(saved);

        log.info("Resent verification for: {}", maskIdentifier(identifier));

        return PendingRegistrationResponse.builder()
                .pendingId(saved.getId())
                .maskedIdentifier(maskIdentifier(identifier))
                .expiresAt(saved.getExpiresAt())
                .remainingResendAttempts(saved.getRemainingResendAttempts())
                .message("Verification email resent. Please check your inbox.")
                .build();
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
