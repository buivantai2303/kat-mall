/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.infrastructure.security;

import com.en.katmall.co.identity.domain.service.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * BCrypt implementation of PasswordEncoder domain service.
 * Uses Spring Security's BCryptPasswordEncoder with default strength.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Component
public class BcryptPasswordEncoder implements PasswordEncoder {

    /**
     * BCrypt password encoder instance.
     */
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Encodes a raw password using BCrypt.
     * 
     * @param rawPassword the raw password to encode
     * @return the encoded password
     */
    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * Checks if the raw password matches the encoded password.
     * 
     * @param rawPassword     the raw password to check
     * @param encodedPassword the encoded password to compare against
     * @return true if the raw password matches the encoded password, false
     *         otherwise
     */
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
