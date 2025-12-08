/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.annotation;

import com.en.katmall.co.shared.validation.validator.KSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates string length or collection size.
 * 
 * <p>
 * Usage:
 * 
 * <pre>
 * &#64;KSize(min = 2, max = 100, field = "name")
 * private String name;
 * </pre>
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = KSizeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface KSize {

    /**
     * Minimum size.
     */
    int min() default 0;

    /**
     * Maximum size.
     */
    int max() default Integer.MAX_VALUE;

    /**
     * The field name for message interpolation.
     */
    String field() default "";

    /**
     * Custom message code.
     */
    String message() default "k.validation.size";

    /**
     * Validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload for additional metadata.
     */
    Class<? extends Payload>[] payload() default {};
}
