/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Configuration class for internationalization (i18n) support.
 * 
 * <p>
 * This configuration enables multi-language support through:
 * <ul>
 * <li>Message source for loading translation files</li>
 * <li>Locale resolver based on Accept-Language header</li>
 * <li>Locale change interceptor for ?lang= parameter</li>
 * <li>Custom header-based locale resolution (X-Language)</li>
 * </ul>
 * 
 * <h2>Supported Locales</h2>
 * <ul>
 * <li>vi (Vietnamese) - Default</li>
 * <li>en (English)</li>
 * </ul>
 * 
 * <h2>Language Selection Priority</h2>
 * <ol>
 * <li>X-Language header (custom header)</li>
 * <li>lang query parameter (?lang=en)</li>
 * <li>Accept-Language header</li>
 * <li>Default locale (Vietnamese)</li>
 * </ol>
 * 
 * @author tai.buivan
 * @version 1.0
 * @see MessageSource
 * @see LocaleResolver
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

    /** Default locale - Vietnamese */
    public static final Locale DEFAULT_LOCALE = new Locale("vi");

    /** List of supported locales */
    public static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
            new Locale("vi"),
            new Locale("en"));

    /** Custom header name for language selection */
    public static final String LANGUAGE_HEADER = "X-Language";

    /**
     * Configures the message source for loading i18n messages.
     * 
     * <p>
     * Messages are loaded from:
     * <ul>
     * <li>classpath:i18n/messages</li>
     * <li>classpath:i18n/validation</li>
     * </ul>
     * 
     * <p>
     * Configuration:
     * <ul>
     * <li>UTF-8 encoding</li>
     * <li>Fallback to system locale disabled</li>
     * <li>Cache timeout: 3600 seconds (1 hour)</li>
     * </ul>
     * 
     * @return Configured MessageSource bean
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        // Set base names for message files (multiple bundles)
        messageSource.setBasenames(
                "classpath:i18n/messages",
                "classpath:i18n/validation");

        // Set default encoding to UTF-8
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

        // Disable fallback to system locale
        messageSource.setFallbackToSystemLocale(false);

        // Cache messages for 1 hour
        messageSource.setCacheSeconds(3600);

        // Use message code as default message
        messageSource.setUseCodeAsDefaultMessage(true);

        return messageSource;
    }

    /**
     * Configures the locale resolver based on Accept-Language header.
     * 
     * <p>
     * This resolver uses a custom implementation that first checks
     * the X-Language header, then falls back to Accept-Language header.
     * 
     * @return Configured LocaleResolver bean
     */
    @Bean
    public LocaleResolver localeResolver() {
        CustomHeaderLocaleResolver resolver = new CustomHeaderLocaleResolver();
        resolver.setDefaultLocale(DEFAULT_LOCALE);
        resolver.setSupportedLocales(SUPPORTED_LOCALES);
        return resolver;
    }

    /**
     * Creates a locale change interceptor to support ?lang= parameter.
     * 
     * <p>
     * This allows changing locale via URL parameter:
     * 
     * <pre>
     * GET /api/products?lang=en
     * GET /api/products?lang=vi
     * </pre>
     * 
     * @return Configured LocaleChangeInterceptor bean
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Registers the locale change interceptor.
     * 
     * @param registry The interceptor registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Configures the validator to use message source for validation messages.
     * 
     * <p>
     * This allows using i18n messages in Bean Validation annotations:
     * 
     * <pre>
     * &#64;NotBlank(message = "{validation.required}")
     * private String name;
     * </pre>
     * 
     * @return Configured LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    /**
     * Custom locale resolver that checks X-Language header first.
     * 
     * <p>
     * Priority order:
     * <ol>
     * <li>X-Language header</li>
     * <li>Accept-Language header</li>
     * <li>Default locale</li>
     * </ol>
     */
    public static class CustomHeaderLocaleResolver extends AcceptHeaderLocaleResolver {

        /**
         * Resolves the locale from request headers.
         * 
         * @param request The HTTP request
         * @return Resolved locale
         */
        @Override
        public Locale resolveLocale(jakarta.servlet.http.HttpServletRequest request) {
            // First, check custom X-Language header
            String languageHeader = request.getHeader(LANGUAGE_HEADER);
            if (languageHeader != null && !languageHeader.isEmpty()) {
                Locale locale = Locale.forLanguageTag(languageHeader);
                if (isSupportedLocale(locale)) {
                    return locale;
                }
            }

            // Fall back to Accept-Language header
            return super.resolveLocale(request);
        }

        /**
         * Checks if the given locale is supported.
         * 
         * @param locale The locale to check
         * @return true if supported
         */
        private boolean isSupportedLocale(Locale locale) {
            return SUPPORTED_LOCALES.stream()
                    .anyMatch(supported -> supported.getLanguage().equals(locale.getLanguage()));
        }
    }
}
