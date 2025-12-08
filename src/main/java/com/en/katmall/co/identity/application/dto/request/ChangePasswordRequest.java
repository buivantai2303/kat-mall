/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.dto.request;

import com.en.katmall.co.shared.validation.annotation.KFieldsMatch;
import com.en.katmall.co.shared.validation.annotation.KNotBlank;
import com.en.katmall.co.shared.validation.annotation.KPassword;
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
@KFieldsMatch(field = "newPassword", fieldMatch = "confirmNewPassword")
public class ChangePasswordRequest {

    /**
     * Current password for verification
     */
    @KNotBlank(field = "currentPassword")
    private String currentPassword;

    /**
     * New password
     */
    @KNotBlank(field = "newPassword")
    @KPassword(minLength = 8)
    private String newPassword;

    /**
     * New password confirmation
     */
    @KNotBlank(field = "confirmNewPassword")
    private String confirmNewPassword;
}
