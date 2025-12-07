/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.enums;

import lombok.Getter;

/**
 * Enumeration of refund transaction statuses.
 * Defines the lifecycle states for refund processing.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public enum KTypeRefundStatus {

    /**
     * Refund request created, awaiting processing
     */
    PENDING("PENDING", "Pending", "Chờ xử lý"),

    /**
     * Refund is being processed by payment gateway
     */
    PROCESSING("PROCESSING", "Processing", "Đang xử lý"),

    /**
     * Refund completed successfully
     */
    COMPLETED("COMPLETED", "Completed", "Hoàn thành"),

    /**
     * Refund processing failed
     */
    FAILED("FAILED", "Failed", "Thất bại");

    private final String code;
    private final String nameEn;
    private final String nameVi;

    KTypeRefundStatus(String code, String nameEn, String nameVi) {
        this.code = code;
        this.nameEn = nameEn;
        this.nameVi = nameVi;
    }

    /**
     * Gets display name based on language
     * 
     * @param languageCode The language code ('vi' or 'en')
     * @return Localized display name
     */
    public String getDisplayName(String languageCode) {
        return "vi".equalsIgnoreCase(languageCode) ? nameVi : nameEn;
    }

    /**
     * Finds enum by code value
     * 
     * @param code The code to search for
     * @return The matching enum value
     * @throws IllegalArgumentException if code not found
     */
    public static KTypeRefundStatus fromCode(String code) {
        for (KTypeRefundStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown refund status code: " + code);
    }

    /**
     * Checks if this status is terminal (final state)
     * 
     * @return true if COMPLETED or FAILED
     */
    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED;
    }
}
