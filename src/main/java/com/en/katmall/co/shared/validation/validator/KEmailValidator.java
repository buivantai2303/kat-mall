/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.validator;

import com.en.katmall.co.shared.validation.annotation.KEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validator for {@link KEmail} annotation.
 * Uses RFC 5322 compliant email pattern.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public class KEmailValidator implements ConstraintValidator<KEmail, String> {

    // RFC 5322 compliant email pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");

    private String messageCode;

    @Override
    public void initialize(KEmail annotation) {
        this.messageCode = annotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null values are valid (use @KNotBlank for null check)
        if (value == null || value.isEmpty()) {
            return true;
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageCode)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
