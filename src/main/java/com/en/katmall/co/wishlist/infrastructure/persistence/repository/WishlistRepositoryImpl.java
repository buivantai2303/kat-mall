/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.wishlist.infrastructure.persistence.repository;

import com.en.katmall.co.wishlist.domain.model.Wishlist;
import com.en.katmall.co.wishlist.domain.repository.WishlistRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of WishlistRepository.
 * TODO: Replace with JPA implementation
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public class WishlistRepositoryImpl implements WishlistRepository {

    private final Map<String, Wishlist> storage = new ConcurrentHashMap<>();
    private final Map<String, String> userWishlistMap = new ConcurrentHashMap<>();

    @Override
    public Wishlist save(Wishlist wishlist) {
        Objects.requireNonNull(wishlist, "Wishlist must not be null");
        storage.put(wishlist.getId(), wishlist);
        userWishlistMap.put(wishlist.getUserId(), wishlist.getId());
        return wishlist;
    }

    @Override
    public Optional<Wishlist> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Wishlist> findByUserId(String userId) {
        String wishlistId = userWishlistMap.get(userId);
        if (wishlistId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(wishlistId));
    }

    @Override
    public Wishlist getOrCreateForUser(String userId) {
        return findByUserId(userId).orElseGet(() -> {
            Wishlist newWishlist = Wishlist.createForUser(userId);
            return save(newWishlist);
        });
    }

    @Override
    public void deleteById(String id) {
        Wishlist wishlist = storage.remove(id);
        if (wishlist != null) {
            userWishlistMap.remove(wishlist.getUserId());
        }
    }

    @Override
    public void deleteByUserId(String userId) {
        String wishlistId = userWishlistMap.remove(userId);
        if (wishlistId != null) {
            storage.remove(wishlistId);
        }
    }

    @Override
    public boolean existsByUserId(String userId) {
        return userWishlistMap.containsKey(userId);
    }
}
