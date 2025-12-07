/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.i18n;

import com.en.katmall.co.shared.infrastructure.config.I18nConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Locale;

/**
 * Filter that sets the locale context for each request.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LocaleContextFilter extends OncePerRequestFilter {

    private final LocaleResolver localeResolver;

    public LocaleContextFilter(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            Locale locale = resolveLocale(request);
            LocaleContextHolder.setLocale(locale);
            response.setHeader("X-Content-Language", locale.getLanguage());
            filterChain.doFilter(request, response);
        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }

    private Locale resolveLocale(HttpServletRequest request) {
        // Priority 1: X-Language header
        String langHeader = request.getHeader(I18nConfig.LANGUAGE_HEADER);
        if (langHeader != null && !langHeader.isEmpty()) {
            Locale locale = Locale.forLanguageTag(langHeader);
            if (isSupportedLocale(locale))
                return locale;
        }

        // Priority 2: lang query parameter
        String langParam = request.getParameter("lang");
        if (langParam != null && !langParam.isEmpty()) {
            Locale locale = new Locale(langParam);
            if (isSupportedLocale(locale))
                return locale;
        }

        // Priority 3: Accept-Language header
        Locale resolved = localeResolver.resolveLocale(request);
        if (isSupportedLocale(resolved))
            return resolved;

        // Priority 4: Default locale
        return I18nConfig.DEFAULT_LOCALE;
    }

    private boolean isSupportedLocale(Locale locale) {
        if (locale == null)
            return false;
        return I18nConfig.SUPPORTED_LOCALES.stream()
                .anyMatch(s -> s.getLanguage().equalsIgnoreCase(locale.getLanguage()));
    }
}
