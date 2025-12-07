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
 * Enum for product status.
 * Defines the lifecycle states of a product.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum KTypeProductStatus {

    DRAFT("draft", "Nháp"),
    ACTIVE("active", "Đang bán"),
    INACTIVE("inactive", "Ngừng bán"),
    OUT_OF_STOCK("out_of_stock", "Hết hàng"),
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
    public static Optional<KTypeProductStatus> fromCode(String code) {
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
    public static KTypeProductStatus fromCodeOrDefault(String code, KTypeProductStatus defaultValue) {
        return fromCode(code).orElse(defaultValue);
    }

    /**
     * Finds enum by name (case-insensitive)
     * 
     * @param name The name to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypeProductStatus> fromName(String name) {
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
     * Checks if product can be purchased
     * 
     * @return true if product is available for sale
     */
    public boolean isPurchasable() {
        return this == ACTIVE;
    }

    /**
     * Checks if product is visible on storefront
     * 
     * @return true if product should be displayed
     */
    public boolean isVisible() {
        return this == ACTIVE || this == OUT_OF_STOCK;
    }
}
