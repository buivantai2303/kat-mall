/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.exception;

/**
 * Exception thrown when a requested entity is not found.
 * Maps to HTTP 404 Not Found status.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public class NotFoundException extends DomainException {

    /**
     * Creates a not found exception for a specific entity
     * 
     * @param entityName The name of the entity type (e.g., "User", "Product")
     * @param id         The identifier that was not found
     */
    public NotFoundException(String entityName, String id) {
        super("NOT_FOUND", String.format("%s with id '%s' not found", entityName, id));
    }

    /**
     * Creates a not found exception with custom message
     * 
     * @param message The error message
     */
    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}
