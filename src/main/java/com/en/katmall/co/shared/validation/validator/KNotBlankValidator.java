/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.validator;

import com.en.katmall.co.shared.validation.annotation.KNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for {@link KNotBlank} annotation.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public class KNotBlankValidator implements ConstraintValidator<KNotBlank, String> {

    private String field;
    private String messageCode;

    @Override
    public void initialize(KNotBlank annotation) {
        this.field = annotation.field();
        this.messageCode = annotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            // Disable default message
            context.disableDefaultConstraintViolation();

            // Build custom message with field parameter
            context.buildConstraintViolationWithTemplate(messageCode)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    public String getField() {
        return field;
    }
}
