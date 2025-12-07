/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.catalog.domain.model.valueobject;

import com.en.katmall.co.shared.domain.ValueObject;
import com.en.katmall.co.shared.exception.ValidationException;

import java.util.Objects;

/**
 * Value Object representing a Stock Keeping Unit (SKU).
 * Automatically normalizes to uppercase.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class Sku extends ValueObject {

    private final String value;

    /**
     * Private constructor - use factory method
     * 
     * @param value The SKU string
     */
    private Sku(String value) {
        this.value = value.toUpperCase();
    }

    /**
     * Creates SKU from a string
     * 
     * @param value The SKU string
     * @return New Sku instance
     * @throws ValidationException if SKU is invalid
     */
    public static Sku of(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("sku", "SKU is required");
        }
        if (value.length() < 3 || value.length() > 100) {
            throw new ValidationException("sku", "SKU must be between 3 and 100 characters");
        }
        return new Sku(value);
    }

    /**
     * Gets the SKU value
     * 
     * @return The SKU string in uppercase
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Sku sku = (Sku) o;
        return Objects.equals(value, sku.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
