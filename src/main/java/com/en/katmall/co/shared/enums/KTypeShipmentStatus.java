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
 * Enum for shipment status values.
 * Tracks the delivery lifecycle of an order shipment.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum KTypeShipmentStatus {

    PENDING("pending", "Chờ lấy hàng"),
    PICKED_UP("picked_up", "Đã lấy hàng"),
    IN_TRANSIT("in_transit", "Đang vận chuyển"),
    OUT_FOR_DELIVERY("out_for_delivery", "Đang giao hàng"),
    DELIVERED("delivered", "Đã giao hàng"),
    FAILED("failed", "Giao hàng thất bại"),
    RETURNED("returned", "Đã trả về");

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
    public static Optional<KTypeShipmentStatus> fromCode(String code) {
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
    public static KTypeShipmentStatus fromCodeOrDefault(String code, KTypeShipmentStatus defaultValue) {
        return fromCode(code).orElse(defaultValue);
    }

    /**
     * Finds enum by name (case-insensitive)
     * 
     * @param name The name to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypeShipmentStatus> fromName(String name) {
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
     * Checks if shipment is in progress
     * 
     * @return true if shipment is being processed
     */
    public boolean isInProgress() {
        return this == PICKED_UP || this == IN_TRANSIT || this == OUT_FOR_DELIVERY;
    }

    /**
     * Checks if this is a final/terminal status
     * 
     * @return true if no further transitions are possible
     */
    public boolean isFinal() {
        return this == DELIVERED || this == FAILED || this == RETURNED;
    }
}
