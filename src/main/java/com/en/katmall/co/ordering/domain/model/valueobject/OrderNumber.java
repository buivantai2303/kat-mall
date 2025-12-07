/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.ordering.domain.model.valueobject;

import com.en.katmall.co.shared.domain.ValueObject;

import java.util.Objects;

/**
 * Value Object representing a human-readable order number.
 * Format: ORD-{timestamp}-{random}
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class OrderNumber extends ValueObject {

    private final String value;

    /**
     * Private constructor - use factory methods
     * 
     * @param value The order number string
     */
    private OrderNumber(String value) {
        this.value = value;
    }

    /**
     * Creates OrderNumber from existing value
     * 
     * @param value The order number string
     * @return New OrderNumber instance
     */
    public static OrderNumber of(String value) {
        return new OrderNumber(value);
    }

    /**
     * Generates a new unique order number
     * 
     * @return New OrderNumber with generated value
     */
    public static OrderNumber generate() {
        long timestamp = System.currentTimeMillis();
        String random = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 4).toUpperCase();
        return new OrderNumber("ORD-" + timestamp + "-" + random);
    }

    /**
     * Gets the order number value
     * 
     * @return The order number string
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
        OrderNumber that = (OrderNumber) o;
        return Objects.equals(value, that.value);
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
