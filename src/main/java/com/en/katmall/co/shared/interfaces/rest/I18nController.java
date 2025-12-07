/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.interfaces.rest;

import com.en.katmall.co.shared.infrastructure.config.I18nConfig;
import com.en.katmall.co.shared.infrastructure.i18n.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for i18n/locale operations.
 * Provides endpoints for language management.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/public/i18n")
@RequiredArgsConstructor
public class I18nController {

    private final MessageService messageService;

    /**
     * Gets current locale information
     */
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentLocale() {
        Locale current = messageService.getCurrentLocale();
        Map<String, Object> response = new HashMap<>();
        response.put("language", current.getLanguage());
        response.put("displayName", current.getDisplayLanguage(current));
        response.put("displayNameEn", current.getDisplayLanguage(Locale.ENGLISH));
        return ResponseEntity.ok(response);
    }

    /**
     * Gets list of supported languages
     */
    @GetMapping("/languages")
    public ResponseEntity<Object> getSupportedLanguages() {
        var languages = I18nConfig.SUPPORTED_LOCALES.stream()
                .map(locale -> {
                    Map<String, String> lang = new HashMap<>();
                    lang.put("code", locale.getLanguage());
                    lang.put("name", locale.getDisplayLanguage(locale));
                    lang.put("nameEn", locale.getDisplayLanguage(Locale.ENGLISH));
                    return lang;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("languages", languages);
        response.put("default", I18nConfig.DEFAULT_LOCALE.getLanguage());
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a specific message by code
     */
    @GetMapping("/message/{code}")
    public ResponseEntity<Map<String, String>> getMessage(@PathVariable String code) {
        Map<String, String> response = new HashMap<>();
        response.put("code", code);
        response.put("message", messageService.get(code));
        response.put("locale", messageService.getCurrentLocale().getLanguage());
        return ResponseEntity.ok(response);
    }

    /**
     * Gets common messages for frontend
     */
    @GetMapping("/common")
    public ResponseEntity<Map<String, String>> getCommonMessages() {
        Map<String, String> messages = new HashMap<>();
        messages.put("success", messageService.get("common.success"));
        messages.put("error", messageService.get("common.error"));
        messages.put("loading", messageService.get("common.loading"));
        messages.put("save", messageService.get("common.save"));
        messages.put("cancel", messageService.get("common.cancel"));
        messages.put("delete", messageService.get("common.delete"));
        messages.put("edit", messageService.get("common.edit"));
        messages.put("create", messageService.get("common.create"));
        messages.put("search", messageService.get("common.search"));
        messages.put("confirm", messageService.get("common.confirm"));
        return ResponseEntity.ok(messages);
    }
}
