/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.annotation;

import com.en.katmall.co.shared.validation.validator.KNotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that the annotated string is not null, empty, or blank.
 * Uses i18n message key for error message.
 * 
 * <p>
 * Usage:
 * 
 * <pre>
 * &#64;KNotBlank(field = "email")
 * private String email;
 * </pre>
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = KNotBlankValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface KNotBlank {

    /**
     * The field name for message interpolation.
     * Will be used to resolve "field.{value}" message key.
     */
    String field() default "";

    /**
     * Custom message code. If not specified, uses "k.validation.not.blank".
     */
    String message() default "k.validation.not.blank";

    /**
     * Validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload for additional metadata.
     */
    Class<? extends Payload>[] payload() default {};
}
