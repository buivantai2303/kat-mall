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
 * Enum for promotion/discount types.
 * Defines types of discounts and promotions.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum KTypeDiscountType {

    PERCENTAGE("percentage", "Phần trăm"),
    FIXED_AMOUNT("fixed_amount", "Số tiền cố định"),
    FREE_SHIPPING("free_shipping", "Miễn phí vận chuyển"),
    BUY_X_GET_Y("buy_x_get_y", "Mua X tặng Y");

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
    public static Optional<KTypeDiscountType> fromCode(String code) {
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
    public static KTypeDiscountType fromCodeOrDefault(String code, KTypeDiscountType defaultValue) {
        return fromCode(code).orElse(defaultValue);
    }

    /**
     * Finds enum by name (case-insensitive)
     * 
     * @param name The name to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypeDiscountType> fromName(String name) {
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
     * Checks if discount affects product price
     * 
     * @return true if discount changes item price
     */
    public boolean affectsItemPrice() {
        return this == PERCENTAGE || this == FIXED_AMOUNT;
    }

    /**
     * Checks if discount affects shipping
     * 
     * @return true if discount changes shipping cost
     */
    public boolean affectsShipping() {
        return this == FREE_SHIPPING;
    }
}
