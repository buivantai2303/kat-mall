/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Registration configuration properties.
 * Binds to 'registration.*' properties.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "registration")
@Data
public class RegistrationProperties {

    /** Verification token expiration in hours */
    private int tokenExpirationHours = 24;

    /** Maximum verification resend attempts per day */
    private int maxResendAttempts = 3;

    /** Cleanup pending registrations after hours */
    private int cleanupAfterHours = 48;

    /** Verification URL template */
    private String verifyUrl = "http://localhost:3000/auth/verify?token={token}";

    /**
     * Generates the verification URL for a token
     * 
     * @param token The verification token
     * @return Full verification URL
     */
    public String generateVerifyUrl(String token) {
        return verifyUrl.replace("{token}", token);
    }
}
