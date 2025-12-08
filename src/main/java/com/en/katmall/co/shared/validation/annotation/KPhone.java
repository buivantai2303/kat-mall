/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.annotation;

import com.en.katmall.co.shared.validation.validator.KPhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that the annotated string is a valid phone number.
 * Supports various phone formats including international.
 * 
 * <p>
 * Usage:
 * 
 * <pre>
 * &#64;KPhone
 * private String phoneNumber;
 * 
 * // Với country code
 * &#64;KPhone(region = "VN")
 * private String phoneNumber;
 * </pre>
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = KPhoneValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface KPhone {

    /**
     * ISO 3166-1 alpha-2 region code for phone validation.
     * Default is empty (accepts international format).
     */
    String region() default "";

    /**
     * Allow null/empty values.
     */
    boolean allowEmpty() default true;

    /**
     * Custom message code.
     */
    String message() default "k.validation.phone.invalid";

    /**
     * Validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload for additional metadata.
     */
    Class<? extends Payload>[] payload() default {};
}
