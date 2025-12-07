/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.promotion.application.usecase;

import com.en.katmall.co.promotion.domain.model.Coupon;
import com.en.katmall.co.promotion.domain.repository.CouponRepository;
import com.en.katmall.co.shared.enums.KTypeDiscountType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Use Case: Create Coupon
 * Creates a new promotional coupon.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreateCouponUseCase {

    private final CouponRepository couponRepository;

    /**
     * Executes the create coupon use case.
     * 
     * @param code              Unique coupon code
     * @param description       Coupon description
     * @param discountType      Type of discount
     * @param discountValue     Discount value
     * @param maxDiscountAmount Maximum discount cap (for percentage)
     * @param minOrderValue     Minimum order value to apply
     * @param maxUsageLimit     Maximum usage count
     * @param startDate         Start validity date
     * @param endDate           End validity date
     * @return Created coupon
     */
    public Coupon execute(String code, String description, KTypeDiscountType discountType,
            BigDecimal discountValue, BigDecimal maxDiscountAmount,
            BigDecimal minOrderValue, Integer maxUsageLimit,
            Instant startDate, Instant endDate) {
        Objects.requireNonNull(code, "code must not be null");

        if (couponRepository.existsByCode(code.toUpperCase())) {
            throw new IllegalArgumentException("Coupon code already exists: " + code);
        }

        Coupon coupon = Coupon.builder()
                .code(code)
                .description(description)
                .discountType(discountType)
                .discountValue(discountValue)
                .maxDiscountAmount(maxDiscountAmount)
                .minOrderValue(minOrderValue)
                .maxUsageLimit(maxUsageLimit)
                .startDate(startDate)
                .endDate(endDate)
                .isActive(true)
                .build();

        log.info("Created coupon {} with type {} and value {}", code, discountType, discountValue);
        return couponRepository.save(coupon);
    }
}
