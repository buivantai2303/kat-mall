/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum for authentication provider types.
 * Defines supported authentication methods in the platform.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum KTypeAuthProvider {

    LOCAL("local", "Email/Mật khẩu"),
    GOOGLE("google", "Google"),
    FACEBOOK("facebook", "Facebook"),
    APPLE("apple", "Apple");

    /** Code value for database storage */
    private final String code;

    /** Vietnamese display name for UI */
    private final String displayName;

    /**
     * Finds enum by code value (case-insensitive)
     * 
     * @param code The code to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypeAuthProvider> fromCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(e -> e.code.equalsIgnoreCase(code))
                .findFirst();
    }

    /**
     * Finds enum by code or returns default value
     * 
     * @param code         The code to search for
     * @param defaultValue Default value if not found
     * @return The found enum or default value
     */
    public static KTypeAuthProvider fromCodeOrDefault(String code, KTypeAuthProvider defaultValue) {
        return fromCode(code).orElse(defaultValue);
    }

    /**
     * Finds enum by name (case-insensitive)
     * 
     * @param name The name to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypeAuthProvider> fromName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(valueOf(name.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Legacy method for backward compatibility
     * 
     * @param value The string value to parse
     * @return The matching enum or LOCAL as default
     */
    public static KTypeAuthProvider fromString(String value) {
        return fromName(value).orElse(LOCAL);
    }

    /**
     * Checks if this is an OAuth provider
     * 
     * @return true if authentication via OAuth
     */
    public boolean isOAuth() {
        return this != LOCAL;
    }

    /**
     * Checks if this requires password
     * 
     * @return true if password authentication
     */
    public boolean requiresPassword() {
        return this == LOCAL;
    }
}
