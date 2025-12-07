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
 * Enum for inventory transaction types.
 * Tracks different types of stock movements.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum KTypeInventoryTransaction {

    RECEIVED("received", "Nhập kho"),
    SOLD("sold", "Bán ra"),
    RETURNED("returned", "Trả hàng"),
    ADJUSTED("adjusted", "Điều chỉnh"),
    DAMAGED("damaged", "Hư hỏng"),
    TRANSFERRED("transferred", "Chuyển kho"),
    RESERVED("reserved", "Đặt trước"),
    RELEASED("released", "Hủy đặt trước");

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
    public static Optional<KTypeInventoryTransaction> fromCode(String code) {
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
    public static KTypeInventoryTransaction fromCodeOrDefault(String code, KTypeInventoryTransaction defaultValue) {
        return fromCode(code).orElse(defaultValue);
    }

    /**
     * Finds enum by name (case-insensitive)
     * 
     * @param name The name to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypeInventoryTransaction> fromName(String name) {
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
     * Checks if transaction increases stock
     * 
     * @return true if stock increases
     */
    public boolean increasesStock() {
        return this == RECEIVED || this == RETURNED || this == RELEASED;
    }

    /**
     * Checks if transaction decreases stock
     * 
     * @return true if stock decreases
     */
    public boolean decreasesStock() {
        return this == SOLD || this == DAMAGED || this == RESERVED;
    }
}
