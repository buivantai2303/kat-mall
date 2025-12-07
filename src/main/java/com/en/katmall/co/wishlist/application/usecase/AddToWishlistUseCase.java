/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.wishlist.application.usecase;

import com.en.katmall.co.wishlist.domain.model.Wishlist;
import com.en.katmall.co.wishlist.domain.model.WishlistItem;
import com.en.katmall.co.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Add Item To Wishlist
 * Adds a product to user's wishlist.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AddToWishlistUseCase {

    private final WishlistRepository wishlistRepository;

    /**
     * Executes the add to wishlist use case.
     * 
     * @param userId    User ID
     * @param productId Product ID
     * @param variantId Variant ID (optional)
     * @return Created wishlist item
     */
    public WishlistItem execute(String userId, String productId, String variantId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(productId, "productId must not be null");

        Wishlist wishlist = wishlistRepository.getOrCreateForUser(userId);
        WishlistItem item = wishlist.addItem(productId, variantId);
        wishlistRepository.save(wishlist);

        log.info("Added product {} to wishlist for user {}", productId, userId);
        return item;
    }
}
