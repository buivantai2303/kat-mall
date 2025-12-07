/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.domain.model;

import com.en.katmall.co.shared.domain.BaseEntity;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * News translation entity for multi-language support.
 * Each NewsTranslation represents content in a specific language.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class NewsTranslation extends BaseEntity<String> {

    private String newsId;
    private String languageCode;
    private String title;
    private String content;

    /** Default constructor for JPA */
    protected NewsTranslation() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private NewsTranslation(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.newsId = Objects.requireNonNull(builder.newsId, "newsId must not be null");
        this.languageCode = Objects.requireNonNull(builder.languageCode, "languageCode must not be null");
        this.title = Objects.requireNonNull(builder.title, "title must not be null");
        this.content = builder.content;
    }

    /**
     * Creates a new builder for NewsTranslation
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Updates the translation content
     * 
     * @param title   New title
     * @param content New content
     */
    public void updateContent(String title, String content) {
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.content = content;
        markAsUpdated();
    }

    /**
     * Builder class for NewsTranslation
     */
    public static class Builder {
        private String id;
        private String newsId;
        private String languageCode;
        private String title;
        private String content;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder newsId(String newsId) {
            this.newsId = newsId;
            return this;
        }

        public Builder languageCode(String languageCode) {
            this.languageCode = languageCode;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        /**
         * Builds the NewsTranslation instance
         * 
         * @return New NewsTranslation instance
         * @throws NullPointerException if required fields are missing
         */
        public NewsTranslation build() {
            return new NewsTranslation(this);
        }
    }
}
