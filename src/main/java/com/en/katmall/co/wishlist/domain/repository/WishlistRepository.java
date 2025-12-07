/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.wishlist.domain.repository;

import com.en.katmall.co.wishlist.domain.model.Wishlist;

import java.util.Optional;

/**
 * Repository interface for Wishlist aggregate root.
 * Defines the contract for wishlist persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface WishlistRepository {

    /**
     * Saves a wishlist
     * 
     * @param wishlist The wishlist to save
     * @return The saved wishlist
     */
    Wishlist save(Wishlist wishlist);

    /**
     * Finds wishlist by ID
     * 
     * @param id The wishlist ID
     * @return Optional containing the wishlist if found
     */
    Optional<Wishlist> findById(String id);

    /**
     * Finds wishlist by user ID
     * 
     * @param userId The user ID
     * @return Optional containing the wishlist if found
     */
    Optional<Wishlist> findByUserId(String userId);

    /**
     * Gets or creates a wishlist for a user
     * 
     * @param userId The user ID
     * @return Existing or new wishlist for the user
     */
    Wishlist getOrCreateForUser(String userId);

    /**
     * Deletes a wishlist by ID
     * 
     * @param id The wishlist ID to delete
     */
    void deleteById(String id);

    /**
     * Deletes a wishlist by user ID
     * 
     * @param userId The user ID
     */
    void deleteByUserId(String userId);

    /**
     * Checks if a user has a wishlist
     * 
     * @param userId The user ID
     * @return true if user has a wishlist
     */
    boolean existsByUserId(String userId);
}
