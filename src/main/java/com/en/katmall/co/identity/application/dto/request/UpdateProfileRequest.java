/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating user profile / personal information.
 * All fields are optional - only provided fields will be updated.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    /**
     * User's first name
     */
    @Size(min = 1, max = 100, message = "{validation.size}")
    private String firstName;

    /**
     * User's last name
     */
    @Size(min = 1, max = 100, message = "{validation.size}")
    private String lastName;

    /**
     * User's full name (alternative to first/last)
     */
    @Size(min = 2, max = 200, message = "{validation.size}")
    private String fullName;

    /**
     * User's phone number
     */
    @Size(max = 20, message = "{validation.max.length}")
    private String phone;

    /**
     * User's avatar URL
     */
    @Size(max = 500, message = "{validation.max.length}")
    private String avatarUrl;

    /**
     * User's birthday (ISO format: yyyy-MM-dd)
     */
    private String birthday;

    /**
     * User's gender: MALE, FEMALE, OTHER
     */
    private String gender;

    /**
     * User's address
     */
    @Size(max = 500, message = "{validation.max.length}")
    private String address;

    /**
     * Gets full name, combining first and last if not directly provided
     * 
     * @return Full name
     */
    public String getEffectiveFullName() {
        if (fullName != null && !fullName.isBlank()) {
            return fullName;
        }
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        if (firstName != null) {
            return firstName;
        }
        if (lastName != null) {
            return lastName;
        }
        return null;
    }
}
