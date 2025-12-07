/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.wishlist.domain.model;

import com.en.katmall.co.shared.domain.BaseEntity;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Wishlist item entity representing a product in a wishlist.
 * Each item links to a product and optionally a specific variant.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class WishlistItem extends BaseEntity<String> {

    private String wishlistId;
    private String productId;
    private String variantId;

    /** Default constructor for JPA */
    protected WishlistItem() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private WishlistItem(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.wishlistId = Objects.requireNonNull(builder.wishlistId, "wishlistId must not be null");
        this.productId = Objects.requireNonNull(builder.productId, "productId must not be null");
        this.variantId = builder.variantId;
    }

    /**
     * Creates a new builder for WishlistItem
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Checks if this item has a specific variant
     * 
     * @return true if variant is specified
     */
    public boolean hasVariant() {
        return variantId != null;
    }

    /**
     * Builder class for WishlistItem
     */
    public static class Builder {
        private String id;
        private String wishlistId;
        private String productId;
        private String variantId;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder wishlistId(String wishlistId) {
            this.wishlistId = wishlistId;
            return this;
        }

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder variantId(String variantId) {
            this.variantId = variantId;
            return this;
        }

        /**
         * Builds the WishlistItem instance
         * 
         * @return New WishlistItem instance
         * @throws NullPointerException if required fields are missing
         */
        public WishlistItem build() {
            return new WishlistItem(this);
        }
    }
}
