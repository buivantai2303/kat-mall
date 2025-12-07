/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.i18n;

import com.en.katmall.co.shared.infrastructure.config.I18nConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Service for retrieving internationalized messages.
 * 
 * <p>
 * This service provides convenient methods for accessing i18n messages
 * from the message source. It automatically uses the current locale from
 * the request context.
 * 
 * <h2>Usage Examples</h2>
 * 
 * <pre>
 * // Get simple message
 * String msg = messageService.get("auth.login.success");
 * 
 * // Get message with parameters
 * String msg = messageService.get("validation.required", "Email");
 * 
 * // Get message with specific locale
 * String msg = messageService.get("common.success", Locale.ENGLISH);
 * 
 * // Get message with default fallback
 * String msg = messageService.getOrDefault("unknown.key", "Default text");
 * </pre>
 * 
 * @author tai.buivan
 * @version 1.0
 * @see MessageSource
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    /** The message source containing all i18n messages */
    private final MessageSource messageSource;

    /**
     * Gets a message by code using the current request locale.
     * 
     * @param code The message code (e.g., "auth.login.success")
     * @return The localized message
     */
    public String get(String code) {
        return messageSource.getMessage(code, null, getCurrentLocale());
    }

    /**
     * Gets a message by code with parameters using the current request locale.
     * 
     * <p>
     * Parameters are substituted into placeholders like {0}, {1}, etc.
     * 
     * @param code The message code
     * @param args The message parameters
     * @return The localized message with substituted parameters
     */
    public String get(String code, Object... args) {
        return messageSource.getMessage(code, args, getCurrentLocale());
    }

    /**
     * Gets a message by code, returning a default if not found.
     * 
     * @param code           The message code
     * @param defaultMessage The default message if code not found
     * @return The localized message or default
     */
    public String getOrDefault(String code, String defaultMessage) {
        return messageSource.getMessage(code, null, defaultMessage, getCurrentLocale());
    }

    /**
     * Gets a message by code with parameters, returning a default if not found.
     * 
     * @param code           The message code
     * @param defaultMessage The default message if code not found
     * @param args           The message parameters
     * @return The localized message or default
     */
    public String getOrDefault(String code, String defaultMessage, Object... args) {
        return messageSource.getMessage(code, args, defaultMessage, getCurrentLocale());
    }

    /**
     * Gets a message by code using a specific locale.
     * 
     * @param code   The message code
     * @param locale The locale to use
     * @return The localized message
     */
    public String get(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

    /**
     * Gets a message by code with parameters using a specific locale.
     * 
     * @param code   The message code
     * @param locale The locale to use
     * @param args   The message parameters
     * @return The localized message
     */
    public String get(String code, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }

    /**
     * Gets a message in English.
     * 
     * @param code The message code
     * @return The English message
     */
    public String getEn(String code) {
        return messageSource.getMessage(code, null, Locale.ENGLISH);
    }

    /**
     * Gets a message in English with parameters.
     * 
     * @param code The message code
     * @param args The message parameters
     * @return The English message
     */
    public String getEn(String code, Object... args) {
        return messageSource.getMessage(code, args, Locale.ENGLISH);
    }

    /**
     * Gets a message in Vietnamese.
     * 
     * @param code The message code
     * @return The Vietnamese message
     */
    public String getVi(String code) {
        return messageSource.getMessage(code, null, I18nConfig.DEFAULT_LOCALE);
    }

    /**
     * Gets a message in Vietnamese with parameters.
     * 
     * @param code The message code
     * @param args The message parameters
     * @return The Vietnamese message
     */
    public String getVi(String code, Object... args) {
        return messageSource.getMessage(code, args, I18nConfig.DEFAULT_LOCALE);
    }

    /**
     * Gets the current locale from the request context.
     * 
     * @return The current locale
     */
    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * Checks if a message code exists.
     * 
     * @param code The message code to check
     * @return true if the message code exists
     */
    public boolean hasMessage(String code) {
        String message = messageSource.getMessage(code, null, null, getCurrentLocale());
        return message != null;
    }

    /**
     * Gets the success message.
     * 
     * @return message
     */
    public String success() {
        return get("common.success");
    }

    /**
     * Gets the error message.
     * 
     * @return message
     */
    public String error() {
        return get("common.error");
    }

    /**
     * Gets a created message.
     * 
     * @param entityName The name of the entity
     * @return Localized created message
     */
    public String createdSuccess(String entityName) {
        return get("success.created", entityName);
    }

    /**
     * Gets an updated message.
     * 
     * @param entityName The name of the entity
     * @return Localized updated message
     */
    public String updatedSuccess(String entityName) {
        return get("success.updated", entityName);
    }

    /**
     * Gets a deleted message.
     * 
     * @param entityName The name of the entity
     * @return Localized deleted message
     */
    public String deletedSuccess(String entityName) {
        return get("success.deleted", entityName);
    }

    /**
     * Gets a not found message.
     * 
     * @return Localized not found message
     */
    public String notFound() {
        return get("error.not.found");
    }

    /**
     * Gets a required message.
     * 
     * @param fieldName The name of the required field
     * @return Localized required message
     */
    public String required(String fieldName) {
        return get("validation.required", fieldName);
    }
}
