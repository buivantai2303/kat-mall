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
 * Response DTO for pending registration.
 * Returned after successful registration before verification.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingRegistrationResponse {

    /** Pending registration ID */
    private String pendingId;

    /** Masked identifier (e.g., u***@email.com) */
    private String maskedIdentifier;

    /** Expiration timestamp */
    private Instant expiresAt;

    /** Remaining resend attempts */
    private int remainingResendAttempts;

    /** Message for the user */
    private String message;
}
