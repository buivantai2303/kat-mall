/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.utils;

import java.util.UUID;

/**
 * Utility class for generating unique identifiers.
 * Provides various ID generation strategies for different use cases.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class IdGenerator {

    /**
     * Private constructor to prevent instantiation
     */
    private IdGenerator() {
    }

    /**
     * Generates a standard UUID string
     * 
     * @return A randomly generated UUID string
     */
    public static String generate() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a short 16-character ID without hyphens
     * 
     * @return A 16-character alphanumeric ID
     */
    public static String generateShort() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * Generates a human-readable order number with timestamp
     * 
     * @return An order number in format "ORD-{timestamp}-{random}"
     */
    public static String generateOrderNumber() {
        long timestamp = System.currentTimeMillis();
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "ORD-" + timestamp + "-" + random;
    }
}
