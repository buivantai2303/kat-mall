/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.wishlist.application.usecase;

import com.en.katmall.co.wishlist.domain.model.Wishlist;
import com.en.katmall.co.wishlist.domain.repository.WishlistRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Get Wishlist
 * Retrieves wishlist information for users.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetWishlistUseCase {

    private final WishlistRepository wishlistRepository;

    /**
     * Gets or creates a wishlist for a user.
     * 
     * @param userId User ID
     * @return User's wishlist
     */
    @Transactional
    public Wishlist getOrCreate(String userId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        return wishlistRepository.getOrCreateForUser(userId);
    }

    /**
     * Finds a wishlist by ID.
     * 
     * @param id Wishlist ID
     * @return The wishlist
     * @throws ResourceNotFoundException if not found
     */
    public Wishlist findById(String id) {
        Objects.requireNonNull(id, "Wishlist ID must not be null");
        return wishlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", id));
    }

    /**
     * Finds a wishlist by user ID.
     * 
     * @param userId User ID
     * @return The wishlist
     * @throws ResourceNotFoundException if not found
     */
    public Wishlist findByUserId(String userId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        return wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "userId", userId));
    }

    /**
     * Checks if a product is in a user's wishlist.
     * 
     * @param userId    User ID
     * @param productId Product ID
     * @param variantId Variant ID (optional)
     * @return true if in wishlist
     */
    public boolean isInWishlist(String userId, String productId, String variantId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        Objects.requireNonNull(productId, "Product ID must not be null");

        return wishlistRepository.findByUserId(userId)
                .map(wishlist -> wishlist.containsProduct(productId, variantId))
                .orElse(false);
    }

    /**
     * Gets the wishlist item count for a user.
     * 
     * @param userId User ID
     * @return Number of items in wishlist
     */
    public int getItemCount(String userId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        return wishlistRepository.findByUserId(userId)
                .map(Wishlist::getItemCount)
                .orElse(0);
    }
}
