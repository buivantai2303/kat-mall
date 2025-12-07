/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.domain.model;

import com.en.katmall.co.shared.domain.AggregateRoot;
import com.en.katmall.co.shared.enums.KTypeBannerPosition;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Banner aggregate root for promotional banner management.
 * Handles display banners on the website with positioning and priority.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class Banner extends AggregateRoot<String> {

    private String name;
    private String imageUrl;
    private String targetUrl;
    private KTypeBannerPosition displayPosition;
    private int priority;
    private boolean isActive;

    /** Default constructor for JPA */
    protected Banner() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private Banner(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.name = Objects.requireNonNull(builder.name, "name must not be null");
        this.imageUrl = Objects.requireNonNull(builder.imageUrl, "imageUrl must not be null");
        this.targetUrl = builder.targetUrl;
        this.displayPosition = builder.displayPosition != null ? builder.displayPosition
                : KTypeBannerPosition.HOME_SLIDER;
        this.priority = builder.priority;
        this.isActive = builder.isActive;
    }

    /**
     * Creates a new builder for Banner
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Activates this banner
     */
    public void activate() {
        this.isActive = true;
        markAsUpdated();
    }

    /**
     * Deactivates this banner
     */
    public void deactivate() {
        this.isActive = false;
        markAsUpdated();
    }

    /**
     * Updates banner content
     * 
     * @param name      Banner name
     * @param imageUrl  Image URL
     * @param targetUrl Target link URL
     */
    public void updateContent(String name, String imageUrl, String targetUrl) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.imageUrl = Objects.requireNonNull(imageUrl, "imageUrl must not be null");
        this.targetUrl = targetUrl;
        markAsUpdated();
    }

    /**
     * Updates display position
     * 
     * @param position New display position
     */
    public void updatePosition(KTypeBannerPosition position) {
        this.displayPosition = Objects.requireNonNull(position, "position must not be null");
        markAsUpdated();
    }

    /**
     * Updates display priority
     * 
     * @param priority New priority (higher = shown first)
     */
    public void updatePriority(int priority) {
        this.priority = priority;
        markAsUpdated();
    }

    /**
     * Builder class for Banner
     */
    public static class Builder {
        private String id;
        private String name;
        private String imageUrl;
        private String targetUrl;
        private KTypeBannerPosition displayPosition = KTypeBannerPosition.HOME_SLIDER;
        private int priority = 0;
        private boolean isActive = true;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder targetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }

        public Builder displayPosition(KTypeBannerPosition displayPosition) {
            this.displayPosition = displayPosition;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        /**
         * Builds the Banner instance
         * 
         * @return New Banner instance
         * @throws NullPointerException if required fields are missing
         */
        public Banner build() {
            return new Banner(this);
        }
    }
}
