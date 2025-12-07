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
 * Response DTO for verification result.
 * Returned after successful email/phone verification.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationResponse {

    /** Created user ID */
    private String userId;

    /** User email (if email identifier) */
    private String email;

    /** User phone (if phone identifier) */
    private String phone;

    /** Success message */
    private String message;

    /** Whether verification was successful */
    private boolean verified;
}
