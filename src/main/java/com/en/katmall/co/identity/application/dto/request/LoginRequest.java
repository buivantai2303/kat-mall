/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for user login.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * User's email address
     */
    @NotBlank(message = "{validation.required}")
    @Email(message = "{validation.email.invalid}")
    private String email;

    /**
     * User's password
     */
    @NotBlank(message = "{validation.required}")
    private String password;

    /**
     * Remember me flag for extended session
     */
    private boolean rememberMe;
}
