/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT configuration properties.
 * Binds to 'jwt.*' properties.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    /** JWT signing secret key */
    private String secret = "your-256-bit-secret-key-here-change-in-production";

    /** Access token expiration in milliseconds */
    private long accessTokenExpiration = 3600000; // 1 hour

    /** Refresh token expiration in milliseconds */
    private long refreshTokenExpiration = 604800000; // 7 days
}
