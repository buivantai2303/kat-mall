/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.exception;

import lombok.Getter;

/**
 * Base exception class for all domain-related exceptions.
 * Provides error code support for client-side error handling.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public class DomainException extends RuntimeException {

    private final String errorCode;

    /**
     * Creates a domain exception with default error code
     * 
     * @param message The error message
     */
    public DomainException(String message) {
        super(message);
        this.errorCode = "DOMAIN_ERROR";
    }

    /**
     * Creates a domain exception with custom error code
     * 
     * @param errorCode The error code for client handling
     * @param message   The error message
     */
    public DomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates a domain exception with error code and cause
     * 
     * @param errorCode The error code for client handling
     * @param message   The error message
     * @param cause     The underlying cause
     */
    public DomainException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
