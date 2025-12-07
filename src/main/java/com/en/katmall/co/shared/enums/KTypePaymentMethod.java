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
 * Enum for payment method types.
 * Defines supported payment methods in the platform.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum KTypePaymentMethod {

    COD("cod", "Thanh toán khi nhận hàng"),
    BANK_TRANSFER("bank_transfer", "Chuyển khoản ngân hàng"),
    MOMO("momo", "Ví MoMo"),
    VNPAY("vnpay", "VNPay"),
    ZALOPAY("zalopay", "ZaloPay"),
    CREDIT_CARD("credit_card", "Thẻ tín dụng"),
    DEBIT_CARD("debit_card", "Thẻ ghi nợ");

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
    public static Optional<KTypePaymentMethod> fromCode(String code) {
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
    public static KTypePaymentMethod fromCodeOrDefault(String code, KTypePaymentMethod defaultValue) {
        return fromCode(code).orElse(defaultValue);
    }

    /**
     * Finds enum by name (case-insensitive)
     * 
     * @param name The name to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypePaymentMethod> fromName(String name) {
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
     * Checks if this is an online payment method
     * 
     * @return true if payment is processed online
     */
    public boolean isOnline() {
        return this != COD;
    }

    /**
     * Checks if this is a cash-based method
     * 
     * @return true if payment is made in cash
     */
    public boolean isCash() {
        return this == COD;
    }
}
