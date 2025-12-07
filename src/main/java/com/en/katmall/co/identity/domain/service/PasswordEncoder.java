/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.domain.service;

/**
 * Domain service interface for password encoding.
 * Abstracts the password hashing implementation from the domain layer.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface PasswordEncoder {

    /**
     * Encodes a raw password using a secure hashing algorithm
     * 
     * @param rawPassword The plain text password
     * @return The encoded password hash
     */
    String encode(String rawPassword);

    /**
     * Verifies a raw password against an encoded password
     * 
     * @param rawPassword     The plain text password to verify
     * @param encodedPassword The stored password hash
     * @return true if passwords match, false otherwise
     */
    boolean matches(String rawPassword, String encodedPassword);
}
