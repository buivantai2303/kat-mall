/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.promotion.application.usecase;

import com.en.katmall.co.promotion.domain.model.Coupon;
import com.en.katmall.co.promotion.domain.repository.CouponRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Use Case: Get Coupon
 * Retrieves coupon information by code or lists coupons.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCouponUseCase {

    private final CouponRepository couponRepository;

    /**
     * Gets a coupon by code.
     * 
     * @param code Coupon code
     * @return The coupon
     * @throws ResourceNotFoundException if not found
     */
    public Coupon executeByCode(String code) {
        Objects.requireNonNull(code, "Coupon code must not be null");
        return couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "code", code));
    }

    /**
     * Gets all valid coupons.
     * 
     * @return List of valid coupons
     */
    public List<Coupon> executeAllValid() {
        return couponRepository.findAllValid();
    }

    /**
     * Gets all coupons with pagination.
     * 
     * @param page Page number
     * @param size Page size
     * @return List of coupons
     */
    public List<Coupon> executeAll(int page, int size) {
        return couponRepository.findAll(page, size);
    }
}
