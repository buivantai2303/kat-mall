/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Principal;

/**
 * Represents an authenticated user in the security context.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class AuthenticatedUser implements Principal {

    private final String id;
    private final String email;
    private final String role;

    @Override
    public String getName() {
        return email;
    }

    /**
     * Checks if user has admin role
     * 
     * @return true if admin
     */
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    /**
     * Checks if user has seller role
     * 
     * @return true if seller
     */
    public boolean isSeller() {
        return "SELLER".equalsIgnoreCase(role);
    }
}
