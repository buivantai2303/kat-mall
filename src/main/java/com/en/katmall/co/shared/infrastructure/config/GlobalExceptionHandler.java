/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.config;

import com.en.katmall.co.shared.dto.ApiResponse;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.exception.NotFoundException;
import com.en.katmall.co.shared.exception.ValidationException;
import com.en.katmall.co.shared.infrastructure.i18n.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST API.
 * Transforms domain exceptions into appropriate HTTP responses.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles NotFoundException - returns HTTP 404
     * 
     * @param ex The caught exception
     * @return Error response with NOT_FOUND status
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getErrorCode(), ex.getMessage(), Instant.now()));
    }

    /**
     * Handles ValidationException - returns HTTP 400 with field errors
     * 
     * @param ex The caught exception
     * @return Validation error response with BAD_REQUEST status
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse(ex.getErrorCode(), ex.getMessage(), ex.getErrors(), Instant.now()));
    }

    /**
     * Handles DomainException - returns HTTP 422 Unprocessable Entity
     * 
     * @param ex The caught exception
     * @return Error response with UNPROCESSABLE_ENTITY status
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(ex.getErrorCode(), ex.getMessage(), Instant.now()));
    }

    /**
     * Handles Bean Validation errors from @Valid annotation
     * 
     * @param ex The caught exception containing validation errors
     * @return Validation error response with field-level errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = (error instanceof FieldError)
                    ? ((FieldError) error).getField()
                    : error.getObjectName();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse(
                        "VALIDATION_ERROR",
                        "Validation failed",
                        errors,
                        Instant.now()));
    }

    /**
     * Handles all other exceptions - returns HTTP 500
     * 
     * @param ex The caught exception
     * @return Generic error response with INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred", Instant.now()));
    }

    /**
     * Standard error response record
     * 
     * @param code      Error code for client handling
     * @param message   Human-readable error message
     * @param timestamp Time when error occurred
     */
    public record ErrorResponse(String code, String message, Instant timestamp) {
    }

    /**
     * Validation error response with field-level errors
     * 
     * @param code      Error code
     * @param message   Summary message
     * @param errors    Map of field names to error messages
     * @param timestamp Time when error occurred
     */
    public record ValidationErrorResponse(String code, String message, Map<String, String> errors, Instant timestamp) {
    }
}
