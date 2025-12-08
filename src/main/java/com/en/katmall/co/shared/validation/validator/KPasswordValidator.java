/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.validator;

import com.en.katmall.co.shared.validation.annotation.KPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for {@link KPassword} annotation.
 * Enforces password strength requirements.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public class KPasswordValidator implements ConstraintValidator<KPassword, String> {

    private int minLength;
    private int maxLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecialChar;
    private String messageCode;

    @Override
    public void initialize(KPassword annotation) {
        this.minLength = annotation.minLength();
        this.maxLength = annotation.maxLength();
        this.requireUppercase = annotation.requireUppercase();
        this.requireLowercase = annotation.requireLowercase();
        this.requireDigit = annotation.requireDigit();
        this.requireSpecialChar = annotation.requireSpecialChar();
        this.messageCode = annotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null values are valid (use @KNotBlank for null check)
        if (value == null || value.isEmpty()) {
            return true;
        }

        StringBuilder errorKey = new StringBuilder();

        // Check length
        if (value.length() < minLength) {
            errorKey.append("k.validation.password.min.length");
        } else if (value.length() > maxLength) {
            errorKey.append("k.validation.password.max.length");
        }

        // Check requirements
        if (requireUppercase && !value.matches(".*[A-Z].*")) {
            if (errorKey.isEmpty())
                errorKey.append("k.validation.password.require.uppercase");
        }
        if (requireLowercase && !value.matches(".*[a-z].*")) {
            if (errorKey.isEmpty())
                errorKey.append("k.validation.password.require.lowercase");
        }
        if (requireDigit && !value.matches(".*\\d.*")) {
            if (errorKey.isEmpty())
                errorKey.append("k.validation.password.require.digit");
        }
        if (requireSpecialChar && !value.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            if (errorKey.isEmpty())
                errorKey.append("k.validation.password.require.special");
        }

        if (!errorKey.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorKey.toString())
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
