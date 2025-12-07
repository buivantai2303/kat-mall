/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.promotion.application.usecase;

import com.en.katmall.co.promotion.domain.model.Coupon;
import com.en.katmall.co.promotion.domain.repository.CouponRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Use Case: Apply Coupon
 * Validates and applies a coupon to an order.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ApplyCouponUseCase {

    private final CouponRepository couponRepository;

    /**
     * Executes the apply coupon use case.
     * Records coupon usage and returns the discount amount.
     * 
     * @param code       Coupon code
     * @param orderValue Order subtotal
     * @return Discount amount applied
     * @throws ResourceNotFoundException if coupon not found
     */
    public BigDecimal execute(String code, BigDecimal orderValue) {
        Objects.requireNonNull(code, "Coupon code must not be null");
        Objects.requireNonNull(orderValue, "Order value must not be null");

        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "code", code));

        BigDecimal discount = coupon.calculateDiscount(orderValue);
        coupon.recordUsage();
        couponRepository.save(coupon);

        log.info("Applied coupon {} with discount {} for order value {}", code, discount, orderValue);
        return discount;
    }
}
