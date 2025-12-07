/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class for JSON serialization and deserialization.
 * Uses Jackson ObjectMapper with common configurations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class JsonUtils {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        // Configure for Java 8 date/time
        MAPPER.registerModule(new JavaTimeModule());
        // Don't fail on unknown properties
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Don't fail on empty beans
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // Write dates as ISO strings, not timestamps
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private JsonUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets the configured ObjectMapper instance
     * 
     * @return ObjectMapper
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }


    /**
     * Converts object to JSON string
     * 
     * @param obj The object to serialize
     * @return JSON string or empty string if fails
     */
    public static String toJson(Object obj) {
        if (obj == null)
            return "null";
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    /**
     * Converts object to JSON string safely
     * 
     * @param obj The object to serialize
     * @return Optional containing JSON string
     */
    public static Optional<String> toJsonSafe(Object obj) {
        if (obj == null)
            return Optional.empty();
        try {
            return Optional.of(MAPPER.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    /**
     * Converts object to pretty-printed JSON string
     * 
     * @param obj The object to serialize
     * @return Pretty JSON string
     */
    public static String toPrettyJson(Object obj) {
        if (obj == null)
            return "null";
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "";
        }
    }


    /**
     * Parses JSON string to object
     * 
     * @param json  The JSON string
     * @param clazz The target class
     * @param <T>   The type
     * @return Parsed object or null if fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (ObjectUtils.isBlank(json))
            return null;
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Parses JSON string to object safely
     * 
     * @param json  The JSON string
     * @param clazz The target class
     * @param <T>   The type
     * @return Optional containing parsed object
     */
    public static <T> Optional<T> fromJsonSafe(String json, Class<T> clazz) {
        if (ObjectUtils.isBlank(json))
            return Optional.empty();
        try {
            return Optional.ofNullable(MAPPER.readValue(json, clazz));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses JSON string to typed object using TypeReference
     * 
     * @param json    The JSON string
     * @param typeRef The type reference
     * @param <T>     The type
     * @return Parsed object or null if fails
     */
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        if (ObjectUtils.isBlank(json))
            return null;
        try {
            return MAPPER.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Parses JSON string to List
     * 
     * @param json         The JSON string
     * @param elementClass Element class
     * @param <T>          Element type
     * @return List or empty list if fails
     */
    public static <T> List<T> fromJsonList(String json, Class<T> elementClass) {
        if (ObjectUtils.isBlank(json))
            return List.of();
        try {
            return MAPPER.readValue(json,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, elementClass));
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }

    /**
     * Parses JSON string to Map
     * 
     * @param json The JSON string
     * @return Map or empty map if fails
     */
    public static Map<String, Object> fromJsonMap(String json) {
        if (ObjectUtils.isBlank(json))
            return Map.of();
        try {
            return MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            return Map.of();
        }
    }


    /**
     * Converts object to another type via JSON
     * 
     * @param obj   Source object
     * @param clazz Target class
     * @param <T>   Target type
     * @return Converted object or null
     */
    public static <T> T convert(Object obj, Class<T> clazz) {
        if (obj == null)
            return null;
        return MAPPER.convertValue(obj, clazz);
    }

    /**
     * Converts object to Map
     * 
     * @param obj The object
     * @return Map representation
     */
    public static Map<String, Object> toMap(Object obj) {
        if (obj == null)
            return Map.of();
        return MAPPER.convertValue(obj, new TypeReference<Map<String, Object>>() {
        });
    }


    /**
     * Checks if string is valid JSON
     * 
     * @param json The string to check
     * @return true if valid JSON
     */
    public static boolean isValidJson(String json) {
        if (ObjectUtils.isBlank(json))
            return false;
        try {
            MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
