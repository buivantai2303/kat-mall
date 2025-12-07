/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.catalog.domain.model;

import com.en.katmall.co.catalog.domain.model.valueobject.Money;
import com.en.katmall.co.catalog.domain.model.valueobject.Slug;
import com.en.katmall.co.shared.domain.AggregateRoot;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Product aggregate root representing a sellable item in the catalog.
 * Contains variants, pricing, and category associations.
 * Supports soft delete for data retention.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class Product extends AggregateRoot<String> {

    private String categoryId;
    private String brandId;
    private Slug slug;
    private Money basePrice;
    private BigDecimal weight;
    private boolean active;
    private Instant deletedAt;

    private List<ProductVariant> variants = new ArrayList<>();

    /** Default constructor for JPA */
    protected Product() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private Product(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.categoryId = Objects.requireNonNull(builder.categoryId, "categoryId must not be null");
        this.slug = Objects.requireNonNull(builder.slug, "slug must not be null");
        this.basePrice = Objects.requireNonNull(builder.basePrice, "basePrice must not be null");
        this.brandId = builder.brandId;
        this.weight = builder.weight;
        this.active = true;
    }

    /**
     * Creates a new builder for Product
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Adds a variant to this product
     * 
     * @param variant The variant to add
     */
    public void addVariant(ProductVariant variant) {
        Objects.requireNonNull(variant, "variant must not be null");
        variants.add(variant);
        markAsUpdated();
    }

    /**
     * Removes a variant from this product
     * 
     * @param variantId The ID of the variant to remove
     */
    public void removeVariant(String variantId) {
        Objects.requireNonNull(variantId, "variantId must not be null");
        variants.removeIf(v -> v.getId().equals(variantId));
        markAsUpdated();
    }

    /**
     * Activates the product for sale
     */
    public void activate() {
        this.active = true;
        markAsUpdated();
    }

    /**
     * Deactivates the product from sale
     */
    public void deactivate() {
        this.active = false;
        markAsUpdated();
    }

    /**
     * Soft deletes the product (keeps data but marks as deleted)
     */
    public void softDelete() {
        this.deletedAt = Instant.now();
        this.active = false;
        markAsUpdated();
    }

    /**
     * Restores a soft-deleted product
     */
    public void restore() {
        this.deletedAt = null;
        this.active = true;
        markAsUpdated();
    }

    /**
     * Updates the base price
     * 
     * @param newPrice The new price
     */
    public void updatePrice(Money newPrice) {
        this.basePrice = Objects.requireNonNull(newPrice, "newPrice must not be null");
        markAsUpdated();
    }

    /**
     * Checks if product is deleted
     * 
     * @return true if soft deleted
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * Gets variants as unmodifiable list
     * 
     * @return Unmodifiable list of variants
     */
    public List<ProductVariant> getVariants() {
        return Collections.unmodifiableList(variants);
    }

    /**
     * Builder class for Product
     */
    public static class Builder {
        private String id;
        private String categoryId;
        private String brandId;
        private Slug slug;
        private Money basePrice;
        private BigDecimal weight;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder categoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder brandId(String brandId) {
            this.brandId = brandId;
            return this;
        }

        public Builder slug(String slug) {
            this.slug = Slug.of(slug);
            return this;
        }

        public Builder slug(Slug slug) {
            this.slug = slug;
            return this;
        }

        public Builder basePrice(BigDecimal price) {
            this.basePrice = Money.of(price);
            return this;
        }

        public Builder basePrice(Money price) {
            this.basePrice = price;
            return this;
        }

        public Builder weight(BigDecimal weight) {
            this.weight = weight;
            return this;
        }

        /**
         * Builds the Product instance
         * 
         * @return New Product instance
         * @throws NullPointerException if required fields are missing
         */
        public Product build() {
            return new Product(this);
        }
    }
}
