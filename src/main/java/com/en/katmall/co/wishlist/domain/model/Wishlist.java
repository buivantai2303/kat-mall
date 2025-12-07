/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.wishlist.domain.model;

import com.en.katmall.co.shared.domain.AggregateRoot;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Wishlist aggregate root for user's favorite products.
 * Manages wishlist items with product and variant tracking.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class Wishlist extends AggregateRoot<String> {

    // Error codes
    private static final String ERR_ITEM_EXISTS = "WISHLIST_ITEM_EXISTS";
    private static final String ERR_ITEM_NOT_FOUND = "WISHLIST_ITEM_NOT_FOUND";
    private static final String ERR_WISHLIST_FULL = "WISHLIST_FULL";

    private static final int MAX_ITEMS = 100;

    private String userId;
    private final List<WishlistItem> items = new ArrayList<>();

    /** Default constructor for JPA */
    protected Wishlist() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private Wishlist(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.userId = Objects.requireNonNull(builder.userId, "userId must not be null");
    }

    /**
     * Creates a new builder for Wishlist
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Factory method to create a new wishlist for a user
     * 
     * @param userId The user ID
     * @return New Wishlist instance
     */
    public static Wishlist createForUser(String userId) {
        return builder().userId(userId).build();
    }

    /**
     * Adds a product to the wishlist
     * 
     * @param productId The product ID
     * @param variantId The variant ID (optional)
     * @return The created WishlistItem
     */
    public WishlistItem addItem(String productId, String variantId) {
        Objects.requireNonNull(productId, "productId must not be null");

        if (items.size() >= MAX_ITEMS) {
            throw new DomainException(ERR_WISHLIST_FULL,
                    String.format("Wishlist cannot contain more than %d items", MAX_ITEMS));
        }

        // Check if item already exists
        boolean exists = items.stream()
                .anyMatch(item -> item.getProductId().equals(productId)
                        && Objects.equals(item.getVariantId(), variantId));
        if (exists) {
            throw new DomainException(ERR_ITEM_EXISTS, "Item already in wishlist");
        }

        WishlistItem item = WishlistItem.builder()
                .wishlistId(this.getId())
                .productId(productId)
                .variantId(variantId)
                .build();
        items.add(item);
        markAsUpdated();
        return item;
    }

    /**
     * Removes an item from the wishlist
     * 
     * @param itemId The wishlist item ID to remove
     */
    public void removeItem(String itemId) {
        boolean removed = items.removeIf(item -> item.getId().equals(itemId));
        if (!removed) {
            throw new DomainException(ERR_ITEM_NOT_FOUND, "Wishlist item not found");
        }
        markAsUpdated();
    }

    /**
     * Removes a product from the wishlist
     * 
     * @param productId The product ID to remove
     * @param variantId The variant ID (optional)
     */
    public void removeProduct(String productId, String variantId) {
        boolean removed = items.removeIf(item -> item.getProductId().equals(productId)
                && Objects.equals(item.getVariantId(), variantId));
        if (!removed) {
            throw new DomainException(ERR_ITEM_NOT_FOUND, "Product not found in wishlist");
        }
        markAsUpdated();
    }

    /**
     * Checks if a product is in the wishlist
     * 
     * @param productId The product ID
     * @param variantId The variant ID (optional)
     * @return true if product is in wishlist
     */
    public boolean containsProduct(String productId, String variantId) {
        return items.stream()
                .anyMatch(item -> item.getProductId().equals(productId)
                        && Objects.equals(item.getVariantId(), variantId));
    }

    /**
     * Gets the number of items in the wishlist
     * 
     * @return Item count
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Clears all items from the wishlist
     */
    public void clear() {
        items.clear();
        markAsUpdated();
    }

    /**
     * Gets an unmodifiable list of items
     * 
     * @return List of wishlist items
     */
    public List<WishlistItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Builder class for Wishlist
     */
    public static class Builder {
        private String id;
        private String userId;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        /**
         * Builds the Wishlist instance
         * 
         * @return New Wishlist instance
         * @throws NullPointerException if required fields are missing
         */
        public Wishlist build() {
            return new Wishlist(this);
        }
    }
}
