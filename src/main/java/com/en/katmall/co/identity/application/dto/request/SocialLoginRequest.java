/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.dto.request;

import com.en.katmall.co.shared.validation.annotation.KEmail;
import com.en.katmall.co.shared.validation.annotation.KNotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for social login/registration.
 * Supports Google, Facebook, Apple authentication.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {

    /**
     * Social provider code: GOOGLE, FACEBOOK, APPLE
     */
    @KNotBlank(field = "provider")
    private String provider;

    /**
     * Access token or ID token from social provider
     */
    @KNotBlank(field = "token")
    private String token;

    /**
     * User's email from social provider (optional, may be extracted from token)
     */
    @KEmail
    private String email;

    /**
     * User's name from social provider
     */
    private String name;

    /**
     * User's avatar URL from social provider
     */
    private String avatarUrl;

    /**
     * Social provider user ID
     */
    private String providerId;
}
