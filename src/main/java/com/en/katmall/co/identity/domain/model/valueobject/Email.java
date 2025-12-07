/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.domain.model.valueobject;

import com.en.katmall.co.shared.domain.ValueObject;
import com.en.katmall.co.shared.exception.ValidationException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing an email address.
 * Immutable and self-validating. Stores email in lowercase.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class Email extends ValueObject {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    /**
     * Private constructor - use factory method
     * 
     * @param value The email string
     */
    private Email(String value) {
        this.value = value.toLowerCase().trim();
    }

    /**
     * Factory method to create Email value object
     * 
     * @param value The email string
     * @return New Email instance
     * @throws ValidationException if email is invalid
     */
    public static Email of(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("email", "Email is required");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new ValidationException("email", "Invalid email format");
        }
        return new Email(value);
    }

    /**
     * Gets the email value
     * 
     * @return The email string in lowercase
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the domain part of the email
     * 
     * @return The domain (part after @)
     */
    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
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
