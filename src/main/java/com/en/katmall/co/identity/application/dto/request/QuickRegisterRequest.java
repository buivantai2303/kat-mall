/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for quick registration.
 * Supports both email and phone number as identifier.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuickRegisterRequest {

    /**
     * User identifier: email or phone number.
     * Will be automatically detected based on format.
     */
    @NotBlank(message = "{validation.required}")
    @Size(max = 255, message = "{validation.max.length}")
    private String identifier;

    /**
     * User password.
     * Must meet security requirements.
     */
    @NotBlank(message = "{validation.required}")
    @Size(min = 8, max = 100, message = "{validation.min.length}")
    private String password;

    /**
     * Password confirmation.
     * Must match password field.
     */
    @NotBlank(message = "{validation.required}")
    private String confirmPassword;
}
