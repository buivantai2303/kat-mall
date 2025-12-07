/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.catalog.domain.model;

import com.en.katmall.co.catalog.domain.model.valueobject.Money;
import com.en.katmall.co.catalog.domain.model.valueobject.Sku;
import com.en.katmall.co.shared.domain.BaseEntity;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Entity representing a specific variant of a product.
 * A product can have multiple variants with different SKUs, prices, and
 * attributes.
 * Belongs to the Product aggregate.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class ProductVariant extends BaseEntity<String> {

    private String productId;
    private Sku sku;
    private Money price;
    private Money compareAtPrice;
    private String imageUrl;
    private boolean active;

    /** Default constructor for JPA */
    protected ProductVariant() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private ProductVariant(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.productId = Objects.requireNonNull(builder.productId, "productId must not be null");
        this.sku = Objects.requireNonNull(builder.sku, "sku must not be null");
        this.price = Objects.requireNonNull(builder.price, "price must not be null");
        this.compareAtPrice = builder.compareAtPrice;
        this.imageUrl = builder.imageUrl;
        this.active = true;
    }

    /**
     * Creates a new builder for ProductVariant
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Updates the variant price
     * 
     * @param newPrice The new price
     */
    public void updatePrice(Money newPrice) {
        this.price = Objects.requireNonNull(newPrice, "newPrice must not be null");
        markAsUpdated();
    }

    /**
     * Sets the compare-at (strikethrough) price
     * 
     * @param compareAtPrice The comparison price
     */
    public void updateCompareAtPrice(Money compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
        markAsUpdated();
    }

    /**
     * Activates the variant for sale
     */
    public void activate() {
        this.active = true;
        markAsUpdated();
    }

    /**
     * Deactivates the variant from sale
     */
    public void deactivate() {
        this.active = false;
        markAsUpdated();
    }

    /**
     * Updates the image URL
     * 
     * @param imageUrl New image URL
     */
    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        markAsUpdated();
    }

    /**
     * Checks if variant has a discount (compare price > current price)
     * 
     * @return true if discounted
     */
    public boolean hasDiscount() {
        return compareAtPrice != null &&
                compareAtPrice.getAmount().compareTo(price.getAmount()) > 0;
    }

    /**
     * Builder class for ProductVariant
     */
    public static class Builder {
        private String id;
        private String productId;
        private Sku sku;
        private Money price;
        private Money compareAtPrice;
        private String imageUrl;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder sku(String sku) {
            this.sku = Sku.of(sku);
            return this;
        }

        public Builder sku(Sku sku) {
            this.sku = sku;
            return this;
        }

        public Builder price(Money price) {
            this.price = price;
            return this;
        }

        public Builder compareAtPrice(Money compareAtPrice) {
            this.compareAtPrice = compareAtPrice;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        /**
         * Builds the ProductVariant instance
         * 
         * @return New ProductVariant instance
         * @throws NullPointerException if required fields are missing
         */
        public ProductVariant build() {
            return new ProductVariant(this);
        }
    }
}
