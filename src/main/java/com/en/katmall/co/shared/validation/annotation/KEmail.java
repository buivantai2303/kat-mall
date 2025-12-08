/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.annotation;

import com.en.katmall.co.shared.validation.validator.KEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that the annotated string is a valid email address.
 * 
 * <p>
 * Usage:
 * 
 * <pre>
 * &#64;KEmail
 * private String email;
 * </pre>
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = KEmailValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface KEmail {

    /**
     * Custom message code.
     */
    String message() default "k.validation.email.invalid";

    /**
     * Validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload for additional metadata.
     */
    Class<? extends Payload>[] payload() default {};
}
