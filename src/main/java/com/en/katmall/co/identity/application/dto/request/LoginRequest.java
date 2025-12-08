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
    @KNotBlank(field = "email")
    @KEmail
    private String email;

    /**
     * User's password
     */
    @KNotBlank(field = "password")
    private String password;

    /**
     * Remember me flag for extended session
     */
    private boolean rememberMe;
}
