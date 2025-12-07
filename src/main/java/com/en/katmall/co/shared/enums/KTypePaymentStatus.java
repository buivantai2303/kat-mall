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
 * Enum for payment status values.
 * Tracks the lifecycle of a payment transaction.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum KTypePaymentStatus {

    PENDING("pending", "Chờ thanh toán"),
    PROCESSING("processing", "Đang xử lý"),
    COMPLETED("completed", "Đã thanh toán"),
    FAILED("failed", "Thanh toán thất bại"),
    REFUNDED("refunded", "Đã hoàn tiền"),
    CANCELLED("cancelled", "Đã hủy");

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
    public static Optional<KTypePaymentStatus> fromCode(String code) {
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
    public static KTypePaymentStatus fromCodeOrDefault(String code, KTypePaymentStatus defaultValue) {
        return fromCode(code).orElse(defaultValue);
    }

    /**
     * Finds enum by name (case-insensitive)
     * 
     * @param name The name to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypePaymentStatus> fromName(String name) {
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
     * Checks if this is a final/terminal status
     * 
     * @return true if no further transitions are possible
     */
    public boolean isFinal() {
        return this == COMPLETED || this == FAILED || this == REFUNDED || this == CANCELLED;
    }

    /**
     * Checks if payment was successful
     * 
     * @return true if payment completed successfully
     */
    public boolean isSuccess() {
        return this == COMPLETED;
    }
}
