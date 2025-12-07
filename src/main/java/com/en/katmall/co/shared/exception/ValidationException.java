/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;

/**
 * Exception thrown when validation fails.
 * Contains field-level error details for client-side form handling.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public class ValidationException extends DomainException {

    private final Map<String, String> errors;

    /**
     * Creates a validation exception for a single field
     * 
     * @param field   The field name that failed validation
     * @param message The validation error message
     */
    public ValidationException(String field, String message) {
        super("VALIDATION_ERROR", message);
        this.errors = Map.of(field, message);
    }

    /**
     * Creates a validation exception with multiple field errors
     * 
     * @param errors Map of field names to error messages
     */
    public ValidationException(Map<String, String> errors) {
        super("VALIDATION_ERROR", "Validation failed");
        this.errors = Collections.unmodifiableMap(errors);
    }

    /**
     * Creates a validation exception with message and field errors
     * 
     * @param message Summary message
     * @param errors  Map of field names to error messages
     */
    public ValidationException(String message, Map<String, String> errors) {
        super("VALIDATION_ERROR", message);
        this.errors = Collections.unmodifiableMap(errors);
    }
}
