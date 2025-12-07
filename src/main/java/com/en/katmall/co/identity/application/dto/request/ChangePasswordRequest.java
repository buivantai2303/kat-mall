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
 * Request DTO for changing password.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    /**
     * Current password for verification
     */
    @NotBlank(message = "{validation.required}")
    private String currentPassword;

    /**
     * New password
     */
    @NotBlank(message = "{validation.required}")
    @Size(min = 8, max = 100, message = "{validation.min.length}")
    private String newPassword;

    /**
     * New password confirmation
     */
    @NotBlank(message = "{validation.required}")
    private String confirmNewPassword;
}
