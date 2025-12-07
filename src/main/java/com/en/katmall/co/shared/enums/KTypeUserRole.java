/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.enums;

/**
 * Enum representing user roles in the system.
 * Used for role-based access control (RBAC).
 * 
 * @author tai.buivan
 * @version 1.0
 */
public enum KTypeUserRole {

    /**
     * Regular customer role.
     * Can browse products, place orders, manage own profile.
     */
    USER("USER", "Regular User"),

    /**
     * Seller/merchant role.
     * Can manage products, view orders, access seller dashboard.
     */
    SELLER("SELLER", "Seller/Merchant"),

    /**
     * Administrator role.
     * Full access to system management features.
     */
    ADMIN("ADMIN", "Administrator"),

    /**
     * Super administrator role.
     * Highest level access, can manage other admins.
     */
    SUPER_ADMIN("SUPER_ADMIN", "Super Administrator"),

    /**
     * Support staff role.
     * Can handle customer inquiries and basic operations.
     */
    SUPPORT("SUPPORT", "Support Staff");

    private final String code;
    private final String description;

    KTypeUserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Gets the role code
     * 
     * @return Role code string
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the role description
     * 
     * @return Human-readable description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Finds role by code
     * 
     * @param code Role code
     * @return KTypeUserRole enum value
     * @throws IllegalArgumentException if code not found
     */
    public static KTypeUserRole fromCode(String code) {
        for (KTypeUserRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role code: " + code);
    }

    /**
     * Checks if this role has admin privileges
     * 
     * @return true if role is ADMIN or SUPER_ADMIN
     */
    public boolean isAdmin() {
        return this == ADMIN || this == SUPER_ADMIN;
    }

    /**
     * Checks if this role can sell products
     * 
     * @return true if role is SELLER or higher
     */
    public boolean canSell() {
        return this == SELLER || isAdmin();
    }
}
