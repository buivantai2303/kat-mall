/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Security configuration properties.
 * Binds to 'security.*' properties.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {

    /** Password requirements */
    private Password password = new Password();

    /** Rate limiting configuration */
    private RateLimit rateLimit = new RateLimit();

    @Data
    public static class Password {
        private int minLength = 8;
        private boolean requireUppercase = true;
        private boolean requireLowercase = true;
        private boolean requireDigit = true;
        private boolean requireSpecial = true;
    }

    @Data
    public static class RateLimit {
        private int registration = 10;
        private int verificationResend = 3;
    }
}
