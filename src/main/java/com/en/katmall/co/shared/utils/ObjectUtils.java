/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Utility class for null checking and object validation.
 * Provides safe operations for handling null values.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class ObjectUtils {

    private ObjectUtils() {
        // Private constructor to prevent instantiation
    }


    /**
     * Checks if an object is null
     * 
     * @param obj The object to check
     * @return true if object is null
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * Checks if an object is not null
     * 
     * @param obj The object to check
     * @return true if object is not null
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * Returns the object if not null, otherwise returns default value
     * 
     * @param obj          The object to check
     * @param defaultValue The default value if null
     * @param <T>          The type of object
     * @return The object or default value
     */
    public static <T> T getOrDefault(T obj, T defaultValue) {
        return obj != null ? obj : defaultValue;
    }

    /**
     * Returns the object if not null, otherwise calls supplier
     * 
     * @param obj      The object to check
     * @param supplier The supplier for default value
     * @param <T>      The type of object
     * @return The object or supplier result
     */
    public static <T> T getOrElse(T obj, Supplier<T> supplier) {
        return obj != null ? obj : supplier.get();
    }

    /**
     * Requires object to be non-null, throws exception if null
     * 
     * @param obj     The object to check
     * @param message The exception message
     * @param <T>     The type of object
     * @return The object if not null
     * @throws IllegalArgumentException if object is null
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }

    /**
     * Wraps object in Optional
     * 
     * @param obj The object to wrap
     * @param <T> The type of object
     * @return Optional containing the object
     */
    public static <T> Optional<T> toOptional(T obj) {
        return Optional.ofNullable(obj);
    }


    /**
     * Checks if string is null or empty
     * 
     * @param str The string to check
     * @return true if null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks if string is not null and not empty
     * 
     * @param str The string to check
     * @return true if has content
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * Checks if string is null, empty, or whitespace only
     * 
     * @param str The string to check
     * @return true if blank
     */
    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    /**
     * Checks if string is not null and has non-whitespace content
     * 
     * @param str The string to check
     * @return true if has non-whitespace content
     */
    public static boolean isNotBlank(String str) {
        return str != null && !str.isBlank();
    }

    /**
     * Returns string or default if blank
     * 
     * @param str          The string to check
     * @param defaultValue The default value
     * @return The string or default
     */
    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    /**
     * Trims string safely, returns null if input is null
     * 
     * @param str The string to trim
     * @return Trimmed string or null
     */
    public static String trimSafe(String str) {
        return str != null ? str.trim() : null;
    }


    /**
     * Checks if collection is null or empty
     * 
     * @param collection The collection to check
     * @return true if null or empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Checks if collection is not null and not empty
     * 
     * @param collection The collection to check
     * @return true if has elements
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * Checks if map is null or empty
     * 
     * @param map The map to check
     * @return true if null or empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Checks if map is not null and not empty
     * 
     * @param map The map to check
     * @return true if has entries
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * Gets collection size safely
     * 
     * @param collection The collection
     * @return Size or 0 if null
     */
    public static int size(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }


    /**
     * Safely compares two objects for equality
     * 
     * @param obj1 First object
     * @param obj2 Second object
     * @return true if both null or equal
     */
    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 == obj2)
            return true;
        if (obj1 == null || obj2 == null)
            return false;
        return obj1.equals(obj2);
    }

    /**
     * Safely compares two strings ignoring case
     * 
     * @param str1 First string
     * @param str2 Second string
     * @return true if both null or equal ignoring case
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == str2)
            return true;
        if (str1 == null || str2 == null)
            return false;
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * Checks if any of the objects are null
     * 
     * @param objects Objects to check
     * @return true if any is null
     */
    public static boolean anyNull(Object... objects) {
        if (objects == null)
            return true;
        for (Object obj : objects) {
            if (obj == null)
                return true;
        }
        return false;
    }

    /**
     * Checks if all objects are not null
     * 
     * @param objects Objects to check
     * @return true if all are not null
     */
    public static boolean allNotNull(Object... objects) {
        return !anyNull(objects);
    }
}
