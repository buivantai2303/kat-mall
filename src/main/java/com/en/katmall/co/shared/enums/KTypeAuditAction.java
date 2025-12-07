/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.enums;

import lombok.Getter;

/**
 * Enumeration of audit action types.
 * Defines the types of operations that can be audited.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public enum KTypeAuditAction {

    /**
     * Record creation action
     */
    CREATE("CREATE", "Create", "Tạo mới"),

    /**
     * Record update action
     */
    UPDATE("UPDATE", "Update", "Cập nhật"),

    /**
     * Record deletion action
     */
    DELETE("DELETE", "Delete", "Xóa");

    private final String code;
    private final String nameEn;
    private final String nameVi;

    KTypeAuditAction(String code, String nameEn, String nameVi) {
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
    public static KTypeAuditAction fromCode(String code) {
        for (KTypeAuditAction action : values()) {
            if (action.code.equalsIgnoreCase(code)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Unknown audit action code: " + code);
    }
}
