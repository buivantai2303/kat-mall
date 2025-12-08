/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Service for internationalized message resolution.
 * Provides convenient methods to get localized messages.
 * 
 * <p>
 * Usage example:
 * 
 * <pre>
 * // Get message with current locale
 * String msg = messageService.get("validation.email.invalid");
 * 
 * // Get message with parameters
 * String msg = messageService.get("validation.min.length", "Password", 8);
 * 
 * // Get message with specific locale
 * String msg = messageService.get("validation.required", Locale.ENGLISH, "Email");
 * </pre>
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class KMessageService {

    private final MessageSource messageSource;

    /**
     * Gets a message for the given code using current locale.
     * 
     * @param code The message code
     * @return The resolved message, or the code itself if not found
     */
    public String get(String code) {
        return get(code, (Object[]) null);
    }

    /**
     * Gets a message for the given code with parameters using current locale.
     * 
     * @param code The message code
     * @param args Arguments to fill placeholders in the message
     * @return The resolved message, or the code itself if not found
     */
    public String get(String code, Object... args) {
        return get(code, LocaleContextHolder.getLocale(), args);
    }

    /**
     * Gets a message for the given code with a specific locale.
     * 
     * @param code   The message code
     * @param locale The locale to use
     * @param args   Arguments to fill placeholders in the message
     * @return The resolved message, or the code itself if not found
     */
    public String get(String code, Locale locale, Object... args) {
        if (code == null) {
            return null;
        }
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            // Return the code itself if message not found
            return code;
        }
    }

    /**
     * Gets a message or returns a default value if not found.
     * 
     * @param code         The message code
     * @param defaultValue Default value if message not found
     * @param args         Arguments to fill placeholders
     * @return The resolved message, or defaultValue if not found
     */
    public String getOrDefault(String code, String defaultValue, Object... args) {
        if (code == null) {
            return defaultValue;
        }
        try {
            return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return defaultValue;
        }
    }

    /**
     * Checks if a message exists for the given code.
     * 
     * @param code The message code
     * @return true if message exists
     */
    public boolean exists(String code) {
        try {
            messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
            return true;
        } catch (NoSuchMessageException e) {
            return false;
        }
    }

    /**
     * Gets the current locale from the context.
     * 
     * @return Current locale
     */
    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }
}
