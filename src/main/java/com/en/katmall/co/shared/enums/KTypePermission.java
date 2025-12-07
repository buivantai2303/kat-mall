/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.enums;

/**
 * Enum representing system permissions.
 * Used for fine-grained permission-based access control.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public enum KTypePermission {

    // User Management
    USER_READ("user:read", "View user information"),
    USER_CREATE("user:create", "Create new users"),
    USER_UPDATE("user:update", "Update user information"),
    USER_DELETE("user:delete", "Delete users"),
    USER_MANAGE("user:manage", "Full user management"),

    // Product Management
    PRODUCT_READ("product:read", "View products"),
    PRODUCT_CREATE("product:create", "Create products"),
    PRODUCT_UPDATE("product:update", "Update products"),
    PRODUCT_DELETE("product:delete", "Delete products"),
    PRODUCT_MANAGE("product:manage", "Full product management"),

    // Order Management
    ORDER_READ("order:read", "View orders"),
    ORDER_CREATE("order:create", "Create orders"),
    ORDER_UPDATE("order:update", "Update orders"),
    ORDER_CANCEL("order:cancel", "Cancel orders"),
    ORDER_MANAGE("order:manage", "Full order management"),

    // Inventory Management
    INVENTORY_READ("inventory:read", "View inventory"),
    INVENTORY_UPDATE("inventory:update", "Update inventory"),
    INVENTORY_MANAGE("inventory:manage", "Full inventory management"),

    // Category Management
    CATEGORY_READ("category:read", "View categories"),
    CATEGORY_CREATE("category:create", "Create categories"),
    CATEGORY_UPDATE("category:update", "Update categories"),
    CATEGORY_DELETE("category:delete", "Delete categories"),
    CATEGORY_MANAGE("category:manage", "Full category management"),

    // Brand Management
    BRAND_READ("brand:read", "View brands"),
    BRAND_CREATE("brand:create", "Create brands"),
    BRAND_UPDATE("brand:update", "Update brands"),
    BRAND_DELETE("brand:delete", "Delete brands"),
    BRAND_MANAGE("brand:manage", "Full brand management"),

    // Review Management
    REVIEW_READ("review:read", "View reviews"),
    REVIEW_CREATE("review:create", "Create reviews"),
    REVIEW_UPDATE("review:update", "Update reviews"),
    REVIEW_DELETE("review:delete", "Delete reviews"),
    REVIEW_MODERATE("review:moderate", "Moderate reviews"),

    // Payment Management
    PAYMENT_READ("payment:read", "View payments"),
    PAYMENT_PROCESS("payment:process", "Process payments"),
    PAYMENT_REFUND("payment:refund", "Process refunds"),
    PAYMENT_MANAGE("payment:manage", "Full payment management"),

    // Report & Analytics
    REPORT_VIEW("report:view", "View reports"),
    ANALYTICS_VIEW("analytics:view", "View analytics"),

    // System Administration
    SYSTEM_CONFIG("system:config", "Manage system configuration"),
    SYSTEM_LOGS("system:logs", "View system logs"),
    SYSTEM_ADMIN("system:admin", "Full system administration");

    private final String code;
    private final String description;

    KTypePermission(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Gets the permission code
     * 
     * @return Permission code string (e.g., "user:read")
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the permission description
     * 
     * @return Human-readable description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the resource part of the permission
     * 
     * @return Resource name (e.g., "user" from "user:read")
     */
    public String getResource() {
        return code.split(":")[0];
    }

    /**
     * Gets the action part of the permission
     * 
     * @return Action name (e.g., "read" from "user:read")
     */
    public String getAction() {
        return code.split(":")[1];
    }

    /**
     * Finds permission by code
     * 
     * @param code Permission code
     * @return KTypePermission enum value
     * @throws IllegalArgumentException if code not found
     */
    public static KTypePermission fromCode(String code) {
        for (KTypePermission permission : values()) {
            if (permission.code.equals(code)) {
                return permission;
            }
        }
        throw new IllegalArgumentException("Unknown permission code: " + code);
    }
}
