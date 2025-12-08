/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.dto.request;

import com.en.katmall.co.shared.validation.annotation.KFieldsMatch;
import com.en.katmall.co.shared.validation.annotation.KNotBlank;
import com.en.katmall.co.shared.validation.annotation.KPassword;
import com.en.katmall.co.shared.validation.annotation.KSize;
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
@KFieldsMatch(field = "password", fieldMatch = "confirmPassword")
public class QuickRegisterRequest {

    /**
     * User identifier: email or phone number.
     * Will be automatically detected based on format.
     */
    @KNotBlank(field = "identifier")
    @KSize(max = 255, field = "identifier")
    private String identifier;

    /**
     * User password.
     * Must meet security requirements.
     */
    @KNotBlank(field = "password")
    @KPassword(minLength = 8)
    private String password;

    /**
     * Password confirmation.
     * Must match password field.
     */
    @KNotBlank(field = "confirmPassword")
    private String confirmPassword;
}
