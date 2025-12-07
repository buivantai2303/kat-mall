/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.domain;

/**
 * Abstract base class for Value Objects in DDD.
 * Value Objects are immutable and compared by their values rather than
 * identity.
 * Subclasses must implement equals(), hashCode(), and toString() methods.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public abstract class ValueObject {

    /**
     * Compares this value object with another by value
     * 
     * @param o The object to compare with
     * @return true if values are equal, false otherwise
     */
    @Override
    public abstract boolean equals(Object o);

    /**
     * Generates hash code based on value properties
     * 
     * @return The hash code
     */
    @Override
    public abstract int hashCode();

    /**
     * Returns string representation of this value object
     * 
     * @return String representation
     */
    @Override
    public abstract String toString();
}
