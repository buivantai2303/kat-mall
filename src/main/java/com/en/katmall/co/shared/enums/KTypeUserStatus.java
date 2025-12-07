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
 * Enum for user account status.
 * Defines the lifecycle states of a user account.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum KTypeUserStatus {

    ACTIVE("active", "Hoạt động"),
    INACTIVE("inactive", "Không hoạt động"),
    LOCKED("locked", "Đã khóa"),
    PENDING_VERIFICATION("pending_verification", "Chờ xác thực"),
    DELETED("deleted", "Đã xóa");

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
    public static Optional<KTypeUserStatus> fromCode(String code) {
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
    public static KTypeUserStatus fromCodeOrDefault(String code, KTypeUserStatus defaultValue) {
        return fromCode(code).orElse(defaultValue);
    }

    /**
     * Finds enum by name (case-insensitive)
     * 
     * @param name The name to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypeUserStatus> fromName(String name) {
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
     * Checks if user can login with this status
     * 
     * @return true if login is allowed
     */
    public boolean canLogin() {
        return this == ACTIVE;
    }

    /**
     * Checks if this is a blocked status
     * 
     * @return true if user is blocked
     */
    public boolean isBlocked() {
        return this == LOCKED || this == DELETED;
    }
}
