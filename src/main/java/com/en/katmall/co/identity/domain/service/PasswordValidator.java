/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.domain.service;

import com.en.katmall.co.shared.exception.ValidationException;
import com.en.katmall.co.shared.infrastructure.config.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Password validation service.
 * Validates password strength based on configurable rules.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class PasswordValidator {

    private final SecurityProperties securityProperties;

    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>\\[\\]\\-_+=~`]");

    /**
     * Validates password strength
     * 
     * @param password The password to validate
     * @throws ValidationException if password doesn't meet requirements
     */
    public void validate(String password) {
        List<String> errors = getValidationErrors(password);
        if (!errors.isEmpty()) {
            throw new ValidationException("WEAK_PASSWORD", "error.password.weak");
        }
    }

    /**
     * Validates password and confirm password match
     * 
     * @param password        The password
     * @param confirmPassword The confirmation password
     * @throws ValidationException if passwords don't match or password is weak
     */
    public void validateWithConfirmation(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new ValidationException("PASSWORD_MISMATCH", "error.password.mismatch");
        }
        validate(password);
    }

    /**
     * Gets list of validation errors for a password
     * 
     * @param password The password to check
     * @return List of error messages (empty if valid)
     */
    public List<String> getValidationErrors(String password) {
        List<String> errors = new ArrayList<>();
        SecurityProperties.Password config = securityProperties.getPassword();

        if (password == null || password.isEmpty()) {
            errors.add("Password is required");
            return errors;
        }

        if (password.length() < config.getMinLength()) {
            errors.add(String.format("Password must have at least %d characters", config.getMinLength()));
        }

        if (config.isRequireUppercase() && !UPPERCASE_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least 1 uppercase letter");
        }

        if (config.isRequireLowercase() && !LOWERCASE_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least 1 lowercase letter");
        }

        if (config.isRequireDigit() && !DIGIT_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least 1 digit");
        }

        if (config.isRequireSpecial() && !SPECIAL_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least 1 special character (!@#$%^&*...)");
        }

        return errors;
    }

    /**
     * Calculates password strength score (0-100)
     * 
     * @param password The password to evaluate
     * @return Strength score
     */
    public int calculateStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        int score = 0;

        // Length score (up to 30 points)
        score += Math.min(password.length() * 3, 30);

        // Character variety score (up to 40 points)
        if (UPPERCASE_PATTERN.matcher(password).find())
            score += 10;
        if (LOWERCASE_PATTERN.matcher(password).find())
            score += 10;
        if (DIGIT_PATTERN.matcher(password).find())
            score += 10;
        if (SPECIAL_PATTERN.matcher(password).find())
            score += 10;

        // Mixed case bonus (10 points)
        if (UPPERCASE_PATTERN.matcher(password).find() && LOWERCASE_PATTERN.matcher(password).find()) {
            score += 10;
        }

        // Number and special char bonus (10 points)
        if (DIGIT_PATTERN.matcher(password).find() && SPECIAL_PATTERN.matcher(password).find()) {
            score += 10;
        }

        return Math.min(score, 100);
    }

    /**
     * Gets password strength label
     * 
     * @param password The password
     * @return Strength label
     */
    public String getStrengthLabel(String password) {
        int score = calculateStrength(password);
        if (score < 30)
            return "Weak";
        if (score < 50)
            return "Fair";
        if (score < 70)
            return "Good";
        return "Strong";
    }
}
