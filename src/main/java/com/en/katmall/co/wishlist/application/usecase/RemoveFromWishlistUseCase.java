/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.wishlist.application.usecase;

import com.en.katmall.co.wishlist.domain.model.Wishlist;
import com.en.katmall.co.wishlist.domain.repository.WishlistRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Remove From Wishlist
 * Removes items or products from user's wishlist.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RemoveFromWishlistUseCase {

    private final WishlistRepository wishlistRepository;

    /**
     * Removes an item from wishlist by item ID.
     * 
     * @param userId User ID
     * @param itemId Wishlist item ID
     * @throws ResourceNotFoundException if wishlist not found
     */
    public void removeItem(String userId, String itemId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        Objects.requireNonNull(itemId, "Item ID must not be null");

        Wishlist wishlist = findByUserId(userId);
        wishlist.removeItem(itemId);
        wishlistRepository.save(wishlist);

        log.info("Removed item {} from wishlist for user {}", itemId, userId);
    }

    /**
     * Removes a product from wishlist.
     * 
     * @param userId    User ID
     * @param productId Product ID
     * @param variantId Variant ID (optional)
     * @throws ResourceNotFoundException if wishlist not found
     */
    public void removeProduct(String userId, String productId, String variantId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        Objects.requireNonNull(productId, "Product ID must not be null");

        Wishlist wishlist = findByUserId(userId);
        wishlist.removeProduct(productId, variantId);
        wishlistRepository.save(wishlist);

        log.info("Removed product {} from wishlist for user {}", productId, userId);
    }

    /**
     * Clears all items from a user's wishlist.
     * 
     * @param userId User ID
     */
    public void clearWishlist(String userId) {
        Objects.requireNonNull(userId, "User ID must not be null");

        wishlistRepository.findByUserId(userId).ifPresent(wishlist -> {
            wishlist.clear();
            wishlistRepository.save(wishlist);
            log.info("Cleared wishlist for user {}", userId);
        });
    }

    /**
     * Deletes a user's entire wishlist.
     * 
     * @param userId User ID
     * @throws ResourceNotFoundException if wishlist not found
     */
    public void deleteWishlist(String userId) {
        Objects.requireNonNull(userId, "User ID must not be null");

        if (!wishlistRepository.existsByUserId(userId)) {
            throw new ResourceNotFoundException("Wishlist", "userId", userId);
        }
        wishlistRepository.deleteByUserId(userId);
        log.info("Deleted wishlist for user {}", userId);
    }

    /**
     * Finds a wishlist by user ID.
     * 
     * @param userId User ID
     * @return The wishlist
     * @throws ResourceNotFoundException if not found
     */
    private Wishlist findByUserId(String userId) {
        return wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "userId", userId));
    }
}
