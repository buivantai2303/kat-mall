/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum for supported language codes.
 * Defines languages available in the platform.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum KTypeLanguage {

    VI("vi", "Tiếng Việt"),
    EN("en", "English"),
    ZH("zh", "中文"),
    JA("ja", "日本語"),
    KO("ko", "한국어");

    /** ISO 639-1 language code */
    private final String code;

    /** Native display name */
    private final String displayName;

    /**
     * Finds enum by code value (case-insensitive)
     * 
     * @param code The code to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypeLanguage> fromCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(e -> e.code.equalsIgnoreCase(code))
                .findFirst();
    }

    /**
     * Finds enum by code or returns default value
     * 
     * @param code         The code to search for
     * @param defaultValue Default value if not found
     * @return The found enum or default value
     */
    public static KTypeLanguage fromCodeOrDefault(String code, KTypeLanguage defaultValue) {
        return fromCode(code).orElse(defaultValue);
    }

    /**
     * Finds enum by name (case-insensitive)
     * 
     * @param name The name to search for
     * @return Optional containing the enum if found
     */
    public static Optional<KTypeLanguage> fromName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(valueOf(name.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets the default language
     * 
     * @return Vietnamese as default
     */
    public static KTypeLanguage getDefault() {
        return VI;
    }

    /**
     * Checks if this is a right-to-left language
     * 
     * @return true if RTL
     */
    public boolean isRtl() {
        return false; // No RTL languages in current list
    }
}
