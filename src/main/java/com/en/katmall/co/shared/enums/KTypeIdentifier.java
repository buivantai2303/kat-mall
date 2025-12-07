/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.enums;

import lombok.Getter;

/**
 * Enumeration of identifier types for registration.
 * Determines whether user registered with email or phone.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public enum KTypeIdentifier {

    /**
     * Email identifier
     */
    EMAIL("EMAIL", "identifier.type.email"),

    /**
     * Phone number identifier
     */
    PHONE("PHONE", "identifier.type.phone");

    private final String code;
    private final String messageKey;

    KTypeIdentifier(String code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    /**
     * Detects identifier type from input string
     * 
     * @param identifier The identifier (email or phone)
     * @return The detected type
     * @throws IllegalArgumentException if format is invalid
     */
    public static KTypeIdentifier detect(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("Identifier cannot be null or blank");
        }

        String trimmed = identifier.trim();

        // Email detection: contains @ and has valid domain format
        if (trimmed.contains("@") && trimmed.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return EMAIL;
        }

        // Phone detection: starts with 0 or +84, and has 10-11 digits
        String phonePattern = "^(\\+84|0)[0-9]{9,10}$";
        if (trimmed.replaceAll("[\\s-]", "").matches(phonePattern)) {
            return PHONE;
        }

        // Default to email if contains @, otherwise throw exception
        if (trimmed.contains("@")) {
            return EMAIL;
        }

        throw new IllegalArgumentException("Invalid identifier format. Must be email or phone number.");
    }

    /**
     * Validates identifier format
     * 
     * @param identifier The identifier to validate
     * @param type       The expected type
     * @return true if valid
     */
    public static boolean isValid(String identifier, KTypeIdentifier type) {
        if (identifier == null || identifier.isBlank()) {
            return false;
        }

        String trimmed = identifier.trim();

        if (type == EMAIL) {
            return trimmed.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        } else if (type == PHONE) {
            String cleaned = trimmed.replaceAll("[\\s-]", "");
            return cleaned.matches("^(\\+84|0)[0-9]{9,10}$");
        }

        return false;
    }

    /**
     * Normalizes phone number to standard format
     * 
     * @param phone The phone number
     * @return Normalized phone (e.g., 0901234567)
     */
    public static String normalizePhone(String phone) {
        if (phone == null)
            return null;
        String cleaned = phone.replaceAll("[\\s-]", "");
        if (cleaned.startsWith("+84")) {
            return "0" + cleaned.substring(3);
        }
        return cleaned;
    }
}
