/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.config;

import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.exception.NotFoundException;
import com.en.katmall.co.shared.exception.ValidationException;
import com.en.katmall.co.shared.infrastructure.i18n.KMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for REST API.
 * Transforms domain exceptions into appropriate HTTP responses.
 * All error messages are resolved through i18n MessageService.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final KMessageService messageService;

    /**
     * Handles NotFoundException - returns HTTP 404
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<KErrorResponse> handleNotFound(NotFoundException ex) {
        String resolvedMessage = messageService.get(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(KErrorResponse.of(ex.getErrorCode(), resolvedMessage));
    }

    /**
     * Handles ValidationException - returns HTTP 400 with field errors
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<KValidationErrorResponse> handleValidation(ValidationException ex) {
        // Resolve main message
        String resolvedMessage = messageService.get("k.validation.error");

        // Resolve each field error message
        Map<String, KFieldError> resolvedErrors = new LinkedHashMap<>();
        ex.getErrors().forEach((field, msgKey) -> {
            String fieldLabel = messageService.getOrDefault("field." + field, field);
            String resolvedFieldMsg = messageService.get(msgKey, fieldLabel);
            resolvedErrors.put(field, new KFieldError(field, fieldLabel, resolvedFieldMsg));
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(KValidationErrorResponse.of(resolvedMessage, resolvedErrors));
    }

    /**
     * Handles DomainException - returns HTTP 422 Unprocessable Entity
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<KErrorResponse> handleDomain(DomainException ex) {
        String resolvedMessage = messageService.get(ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(KErrorResponse.of(ex.getErrorCode(), resolvedMessage));
    }

    /**
     * Handles Bean Validation errors from @Valid annotation.
     * Resolves message codes to actual messages using i18n.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<KValidationErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        Map<String, KFieldError> resolvedErrors = new LinkedHashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = (error instanceof FieldError)
                    ? ((FieldError) error).getField()
                    : error.getObjectName();

            String messageCode = error.getDefaultMessage();

            // Get field label from i18n
            String fieldLabel = messageService.getOrDefault("field." + fieldName, fieldName);

            // Resolve message - if it's a message code, resolve it; otherwise use as-is
            String resolvedMessage;
            if (messageCode != null && messageCode.startsWith("k.validation.")) {
                resolvedMessage = messageService.get(messageCode, fieldLabel);
            } else {
                // Try to resolve, fallback to the message itself
                resolvedMessage = messageService.getOrDefault(messageCode, messageCode, fieldLabel);
            }

            resolvedErrors.put(fieldName, new KFieldError(fieldName, fieldLabel, resolvedMessage));
        });

        String summaryMessage = messageService.get("k.validation.error");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(KValidationErrorResponse.of(summaryMessage, resolvedErrors));
    }

    /**
     * Handles all other exceptions - returns HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<KErrorResponse> handleGeneral(Exception ex) {
        log.error("Unhandled exception", ex);
        String resolvedMessage = messageService.get("error.internal");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(KErrorResponse.of("INTERNAL_ERROR", resolvedMessage));
    }

    // ============================================================
    // RESPONSE DTOs - Clean, non-redundant structure
    // ============================================================

    /**
     * Standard error response
     * 
     * @param code      Error code for client handling (e.g., "NOT_FOUND",
     *                  "VALIDATION_ERROR")
     * @param message   Human-readable, localized error message
     * @param timestamp Time when error occurred
     */
    public record KErrorResponse(
            String code,
            String message,
            Instant timestamp) {
        public static KErrorResponse of(String code, String message) {
            return new KErrorResponse(code, message, Instant.now());
        }
    }

    /**
     * Field-level error details
     * 
     * @param field   Field name (technical, e.g., "email")
     * @param label   Field label (localized, e.g., "Email")
     * @param message Error message (localized)
     */
    public record KFieldError(
            String field,
            String label,
            String message) {
    }

    /**
     * Validation error response with field-level errors.
     * Clean structure without redundant information.
     * 
     * @param code      Always "VALIDATION_ERROR"
     * @param message   Summary message (localized)
     * @param errors    Map of field names to error details
     * @param timestamp Time when error occurred
     */
    public record KValidationErrorResponse(
            String code,
            String message,
            Map<String, KFieldError> errors,
            Instant timestamp) {
        public static KValidationErrorResponse of(String message, Map<String, KFieldError> errors) {
            return new KValidationErrorResponse("VALIDATION_ERROR", message, errors, Instant.now());
        }
    }
}
