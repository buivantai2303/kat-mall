/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Extends NotFoundException with additional field-based lookup support.
 * Maps to HTTP 404 Not Found status.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public class ResourceNotFoundException extends NotFoundException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    /**
     * Creates a resource not found exception for ID-based lookup
     * 
     * @param resourceName The name of the resource (e.g., "User", "Product")
     * @param id           The identifier that was not found
     */
    public ResourceNotFoundException(String resourceName, String id) {
        super(resourceName, id);
        this.resourceName = resourceName;
        this.fieldName = "id";
        this.fieldValue = id;
    }

    /**
     * Creates a resource not found exception for field-based lookup
     * 
     * @param resourceName The name of the resource
     * @param fieldName    The name of the field used for lookup
     * @param fieldValue   The value that was not found
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Gets the resource name
     * 
     * @return Resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Gets the field name used for lookup
     * 
     * @return Field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Gets the field value that was not found
     * 
     * @return Field value
     */
    public Object getFieldValue() {
        return fieldValue;
    }
}
