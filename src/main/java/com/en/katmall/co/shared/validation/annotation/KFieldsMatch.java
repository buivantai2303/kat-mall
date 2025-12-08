/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.annotation;

import com.en.katmall.co.shared.validation.validator.KFieldsMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Class-level validation annotation to ensure two fields have matching values.
 * Commonly used for password confirmation validation.
 * 
 * <p>
 * Usage on class:
 * 
 * <pre>
 * &#64;KFieldsMatch(field = "password", fieldMatch = "confirmPassword")
 * public class RegisterRequest {
 *     private String password;
 *     private String confirmPassword;
 * }
 * </pre>
 * 
 * <p>
 * Multiple field matches:
 * 
 * <pre>
 * &#64;KFieldsMatch.List({
 *     &#64;KFieldsMatch(field = "password", fieldMatch = "confirmPassword"),
 *     &#64;KFieldsMatch(field = "email", fieldMatch = "confirmEmail")
 * })
 * public class RegisterRequest { ... }
 * </pre>
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = KFieldsMatchValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface KFieldsMatch {

    /**
     * The name of the first field.
     */
    String field();

    /**
     * The name of the field to match against.
     */
    String fieldMatch();

    /**
     * Custom message code.
     * Defaults to password mismatch message.
     */
    String message() default "k.validation.password.mismatch";

    /**
     * Validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload for additional metadata.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Allows multiple annotations on the same class.
     */
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        KFieldsMatch[] value();
    }
}
