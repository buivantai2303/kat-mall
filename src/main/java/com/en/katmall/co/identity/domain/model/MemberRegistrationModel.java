/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.domain.model;

import com.en.katmall.co.shared.domain.BaseEntity;
import com.en.katmall.co.shared.enums.KTypeIdentifier;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Objects;

/**
 * Member registration model for temporary storage during registration.
 * Stores registration data until email/phone verification is complete.
 * Prevents spam by limiting resend attempts.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRegistrationModel extends BaseEntity<String> {

    private static final int TOKEN_LENGTH = 32;
    private static final int MAX_RESEND_ATTEMPTS = 3;

    private String identifier;
    private KTypeIdentifier identifierType;
    private String passwordHash;
    private String verificationToken;
    private int verificationAttempts;
    private Instant expiresAt;
    private Instant lastSentAt;
    private boolean verified;

    @Builder
    private MemberRegistrationModel(String id, String identifier, KTypeIdentifier identifierType,
            String passwordHash, int expirationHours) {
        super(id != null ? id : IdGenerator.generate());
        this.identifier = Objects.requireNonNull(identifier, "identifier must not be null");
        this.identifierType = Objects.requireNonNull(identifierType, "identifierType must not be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash must not be null");
        this.verificationToken = generateToken();
        this.verificationAttempts = 1;
        this.expiresAt = Instant.now().plus(expirationHours > 0 ? expirationHours : 24, ChronoUnit.HOURS);
        this.lastSentAt = Instant.now();
        this.verified = false;
    }

    /**
     * Generates a secure random token
     * 
     * @return Base64 URL-safe encoded token
     */
    private static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[TOKEN_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Checks if this registration has expired
     * 
     * @return true if expired
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    /**
     * Checks if resend is allowed
     * 
     * @return true if can resend
     */
    public boolean canResend() {
        return verificationAttempts < MAX_RESEND_ATTEMPTS;
    }

    /**
     * Regenerates the verification token for resend
     * 
     * @param expirationHours Hours until new expiration
     * @throws DomainException if resend limit exceeded or already verified
     */
    public void regenerateToken(int expirationHours) {
        if (!canResend()) {
            throw new DomainException("RESEND_LIMIT_EXCEEDED",
                    "error.registration.resend.limit.exceeded");
        }
        if (verified) {
            throw new DomainException("ALREADY_VERIFIED", "error.registration.already.verified");
        }

        this.verificationToken = generateToken();
        this.verificationAttempts++;
        this.expiresAt = Instant.now().plus(expirationHours, ChronoUnit.HOURS);
        this.lastSentAt = Instant.now();
        markAsUpdated();
    }

    /**
     * Marks this registration as verified
     * 
     * @throws DomainException if expired or already verified
     */
    public void verify() {
        if (verified) {
            throw new DomainException("ALREADY_VERIFIED", "error.registration.already.verified");
        }
        if (isExpired()) {
            throw new DomainException("TOKEN_EXPIRED", "error.registration.token.expired");
        }

        this.verified = true;
        markAsUpdated();
    }

    /**
     * Gets the email if identifier is email type
     * 
     * @return Email or null if phone
     */
    public String getEmail() {
        return identifierType == KTypeIdentifier.EMAIL ? identifier : null;
    }

    /**
     * Gets the phone if identifier is phone type
     * 
     * @return Phone or null if email
     */
    public String getPhone() {
        return identifierType == KTypeIdentifier.PHONE ? identifier : null;
    }

    /**
     * Gets remaining resend attempts
     * 
     * @return Number of remaining attempts
     */
    public int getRemainingResendAttempts() {
        return Math.max(0, MAX_RESEND_ATTEMPTS - verificationAttempts);
    }
}
