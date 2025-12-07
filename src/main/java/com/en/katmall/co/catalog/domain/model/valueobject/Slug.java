/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.catalog.domain.model.valueobject;

import com.en.katmall.co.shared.domain.ValueObject;
import com.en.katmall.co.shared.exception.ValidationException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing a URL-friendly slug for SEO.
 * Automatically normalizes input to lowercase with hyphens.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class Slug extends ValueObject {

    private static final Pattern SLUG_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

    private final String value;

    /**
     * Private constructor - use factory methods
     * 
     * @param value The slug string
     */
    private Slug(String value) {
        this.value = value;
    }

    /**
     * Creates Slug from a pre-formatted string
     * 
     * @param value The slug string
     * @return New Slug instance
     * @throws ValidationException if slug is invalid
     */
    public static Slug of(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("slug", "Slug is required");
        }
        String normalized = normalize(value);
        if (!SLUG_PATTERN.matcher(normalized).matches()) {
            throw new ValidationException("slug", "Invalid slug format");
        }
        return new Slug(normalized);
    }

    /**
     * Creates Slug from a title string
     * 
     * @param title The title to convert
     * @return New Slug instance
     * @throws ValidationException if title is invalid
     */
    public static Slug fromTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ValidationException("slug", "Title is required to generate slug");
        }
        return new Slug(normalize(title));
    }

    /**
     * Normalizes input to valid slug format
     * 
     * @param input The input string
     * @return Normalized slug string
     */
    private static String normalize(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    /**
     * Gets the slug value
     * 
     * @return The slug string
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Slug slug = (Slug) o;
        return Objects.equals(value, slug.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
