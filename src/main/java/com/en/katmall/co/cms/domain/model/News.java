/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.domain.model;

import com.en.katmall.co.shared.domain.AggregateRoot;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.*;

/**
 * News aggregate root for CMS content management.
 * Handles news articles with multi-language support.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class News extends AggregateRoot<String> {

    private String slug;
    private String thumbnailUrl;
    private String authorId;
    private Instant publishedAt;
    private boolean isPublished;
    private final Map<String, NewsTranslation> translations = new HashMap<>();

    /** Default constructor for JPA */
    protected News() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private News(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.slug = Objects.requireNonNull(builder.slug, "slug must not be null");
        this.thumbnailUrl = builder.thumbnailUrl;
        this.authorId = builder.authorId;
        this.publishedAt = builder.publishedAt;
        this.isPublished = builder.isPublished;
    }

    /**
     * Creates a new builder for News
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Adds or updates a translation for this news article
     * 
     * @param languageCode The language code (e.g., 'vi', 'en')
     * @param title        The title in the specified language
     * @param content      The content in the specified language
     */
    public void addTranslation(String languageCode, String title, String content) {
        Objects.requireNonNull(languageCode, "languageCode must not be null");
        Objects.requireNonNull(title, "title must not be null");

        NewsTranslation translation = NewsTranslation.builder()
                .newsId(this.getId())
                .languageCode(languageCode)
                .title(title)
                .content(content)
                .build();
        translations.put(languageCode, translation);
        markAsUpdated();
    }

    /**
     * Removes a translation for the specified language
     * 
     * @param languageCode The language code to remove
     */
    public void removeTranslation(String languageCode) {
        translations.remove(languageCode);
        markAsUpdated();
    }

    /**
     * Gets translation for a specific language
     * 
     * @param languageCode The language code
     * @return Optional containing the translation if found
     */
    public Optional<NewsTranslation> getTranslation(String languageCode) {
        return Optional.ofNullable(translations.get(languageCode));
    }

    /**
     * Publishes this news article
     */
    public void publish() {
        this.isPublished = true;
        this.publishedAt = Instant.now();
        markAsUpdated();
    }

    /**
     * Unpublishes this news article
     */
    public void unpublish() {
        this.isPublished = false;
        markAsUpdated();
    }

    /**
     * Updates the thumbnail URL
     * 
     * @param thumbnailUrl New thumbnail URL
     */
    public void updateThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        markAsUpdated();
    }

    /**
     * Updates the slug
     * 
     * @param slug New URL-friendly identifier
     */
    public void updateSlug(String slug) {
        this.slug = Objects.requireNonNull(slug, "slug must not be null");
        markAsUpdated();
    }

    /**
     * Builder class for News
     */
    public static class Builder {
        private String id;
        private String slug;
        private String thumbnailUrl;
        private String authorId;
        private Instant publishedAt;
        private boolean isPublished = false;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public Builder thumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Builder authorId(String authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder publishedAt(Instant publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public Builder isPublished(boolean isPublished) {
            this.isPublished = isPublished;
            return this;
        }

        /**
         * Builds the News instance
         * 
         * @return New News instance
         * @throws NullPointerException if required fields are missing
         */
        public News build() {
            return new News(this);
        }
    }
}
