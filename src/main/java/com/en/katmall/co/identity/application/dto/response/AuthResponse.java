/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for authentication operations.
 * Contains JWT token and user info.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /** Default token type */
    private static final String DEFAULT_TOKEN_TYPE = "Bearer";

    /** JWT access token */
    private String accessToken;

    /** JWT refresh token */
    private String refreshToken;

    /** Token type (Bearer) */
    @Builder.Default
    private String tokenType = DEFAULT_TOKEN_TYPE;

    /** Token expiration time in seconds */
    private long expiresIn;

    /** User information */
    private UserResponse user;
}
