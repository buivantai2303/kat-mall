/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.validator;

import com.en.katmall.co.shared.validation.annotation.KPhone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validator for {@link KPhone} annotation.
 * Supports Vietnamese and international phone formats.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public class KPhoneValidator implements ConstraintValidator<KPhone, String> {

    // Vietnamese phone: 0[3,5,7,8,9]xxxxxxxx or +84[3,5,7,8,9]xxxxxxxx
    private static final Pattern VN_PHONE = Pattern.compile(
            "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$");

    // International E.164 format
    private static final Pattern INTL_PHONE = Pattern.compile(
            "^\\+[1-9]\\d{6,14}$");

    // General phone (digits, spaces, dashes, parentheses allowed)
    private static final Pattern GENERAL_PHONE = Pattern.compile(
            "^[0-9\\s\\-\\(\\)\\+]{8,20}$");

    private String region;
    private boolean allowEmpty;
    private String messageCode;

    @Override
    public void initialize(KPhone annotation) {
        this.region = annotation.region();
        this.allowEmpty = annotation.allowEmpty();
        this.messageCode = annotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Handle null/empty
        if (value == null || value.isEmpty()) {
            return allowEmpty;
        }

        // Remove spaces for validation
        String phone = value.replaceAll("\\s+", "");

        boolean valid;
        if ("VN".equalsIgnoreCase(region)) {
            valid = VN_PHONE.matcher(phone).matches();
        } else if (!region.isEmpty()) {
            // For other specific regions, use general pattern
            valid = GENERAL_PHONE.matcher(phone).matches();
        } else {
            // Accept Vietnamese or international format
            valid = VN_PHONE.matcher(phone).matches() ||
                    INTL_PHONE.matcher(phone).matches() ||
                    GENERAL_PHONE.matcher(phone).matches();
        }

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageCode)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
