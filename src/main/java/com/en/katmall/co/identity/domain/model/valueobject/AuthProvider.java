/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.domain.model.valueobject;

/**
 * Enumeration of supported authentication providers.
 * LOCAL for email/password, others for OAuth providers.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public enum AuthProvider {
    /** Local email/password authentication */
    LOCAL,
    /** Google OAuth authentication */
    GOOGLE,
    /** Facebook OAuth authentication */
    FACEBOOK,
    /** Apple Sign-In authentication */
    APPLE;

    /**
     * Parses string to AuthProvider, defaults to LOCAL
     * 
     * @param value The string value
     * @return Corresponding AuthProvider or LOCAL if not found
     */
    public static AuthProvider fromString(String value) {
        if (value == null)
            return LOCAL;
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LOCAL;
        }
    }
}
