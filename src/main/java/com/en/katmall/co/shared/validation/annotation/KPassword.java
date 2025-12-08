/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.annotation;

import com.en.katmall.co.shared.validation.validator.KPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that the annotated string meets password strength requirements.
 * 
 * <p>
 * Default requirements:
 * <ul>
 * <li>Minimum 8 characters</li>
 * <li>At least one uppercase letter</li>
 * <li>At least one lowercase letter</li>
 * <li>At least one digit</li>
 * </ul>
 * 
 * <p>
 * Usage:
 * 
 * <pre>
 * &#64;KPassword
 * private String password;
 * 
 * // Với custom requirements
 * &#64;KPassword(minLength = 12, requireSpecialChar = true)
 * private String password;
 * </pre>
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = KPasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface KPassword {

    /**
     * Minimum password length.
     */
    int minLength() default 8;

    /**
     * Maximum password length.
     */
    int maxLength() default 128;

    /**
     * Require at least one uppercase letter.
     */
    boolean requireUppercase() default true;

    /**
     * Require at least one lowercase letter.
     */
    boolean requireLowercase() default true;

    /**
     * Require at least one digit.
     */
    boolean requireDigit() default true;

    /**
     * Require at least one special character.
     */
    boolean requireSpecialChar() default false;

    /**
     * Custom message code.
     */
    String message() default "k.validation.password.weak";

    /**
     * Validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload for additional metadata.
     */
    Class<? extends Payload>[] payload() default {};
}
