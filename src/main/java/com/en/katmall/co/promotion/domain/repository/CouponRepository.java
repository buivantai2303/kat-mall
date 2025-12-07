/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.promotion.domain.repository;

import com.en.katmall.co.promotion.domain.model.Coupon;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Coupon aggregate root.
 * Defines the contract for coupon persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface CouponRepository {

    /**
     * Saves a coupon
     * 
     * @param coupon The coupon to save
     * @return The saved coupon
     */
    Coupon save(Coupon coupon);

    /**
     * Finds coupon by code
     * 
     * @param code The coupon code
     * @return Optional containing the coupon if found
     */
    Optional<Coupon> findByCode(String code);

    /**
     * Finds all active coupons
     * 
     * @return List of active coupons
     */
    List<Coupon> findAllActive();

    /**
     * Finds all valid coupons (active and within date range)
     * 
     * @return List of valid coupons
     */
    List<Coupon> findAllValid();

    /**
     * Finds all coupons (paginated)
     * 
     * @param page Page number (0-indexed)
     * @param size Page size
     * @return List of coupons
     */
    List<Coupon> findAll(int page, int size);

    /**
     * Deletes a coupon by code
     * 
     * @param code The coupon code to delete
     */
    void deleteByCode(String code);

    /**
     * Checks if a coupon code exists
     * 
     * @param code The code to check
     * @return true if code exists
     */
    boolean existsByCode(String code);
}
