/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Response DTO for user information.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    /** User unique identifier */
    private String id;

    /** User's email address */
    private String email;

    /** User's full name */
    private String fullName;

    /** User's phone number */
    private String phone;

    /** User's avatar URL */
    private String avatarUrl;

    /** Whether email is verified */
    private boolean emailVerified;

    /** User's role */
    private String role;

    /** Account creation timestamp */
    private Instant createdAt;

    /** Last login timestamp */
    private Instant lastLoginAt;
}
