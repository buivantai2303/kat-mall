/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.enums;

import lombok.Getter;

/**
 * Enumeration of actor types for audit logging.
 * Defines who can perform auditable actions.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public enum KTypeActorType {

    /**
     * Regular user of the system
     */
    USER("USER", "User", "Người dùng"),

    /**
     * Administrator
     */
    ADMIN("ADMIN", "Administrator", "Quản trị viên"),

    /**
     * System automated action
     */
    SYSTEM("SYSTEM", "System", "Hệ thống");

    private final String code;
    private final String nameEn;
    private final String nameVi;

    KTypeActorType(String code, String nameEn, String nameVi) {
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
    public static KTypeActorType fromCode(String code) {
        for (KTypeActorType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown actor type code: " + code);
    }
}
