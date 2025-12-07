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
 * Enum for notification types.
 * Categorizes different types of user notifications.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum KTypeNotification {

    ORDER("order", "Đơn hàng"),
    PROMOTION("promotion", "Khuyến mãi"),
    SYSTEM("system", "Hệ thống"),
    ACCOUNT("account", "Tài khoản"),
    PAYMENT("payment", "Thanh toán"),
    SHIPPING("shipping", "Vận chuyển");

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
    public static Optional<KTypeNotification> fromCode(String code) {
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
    public static KTypeNotification fromCodeOrDefault(String code, KTypeNotification defaultValue) {
        return fromCode(code).orElse(defaultValue);
    }

    /**
     * Finds enum by name (case-insensitive)
     * 
     * @param name The name to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypeNotification> fromName(String name) {
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
     * Checks if this is a marketing notification
     * 
     * @return true if notification is promotional
     */
    public boolean isMarketing() {
        return this == PROMOTION;
    }

    /**
     * Checks if this is a transactional notification
     * 
     * @return true if related to transactions
     */
    public boolean isTransactional() {
        return this == ORDER || this == PAYMENT || this == SHIPPING;
    }
}
