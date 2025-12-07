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

import java.time.Instant;
import java.util.Objects;

/**
 * Use Case: Update Coupon
 * Updates coupon properties like validity period and activation status.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UpdateCouponUseCase {

    private final CouponRepository couponRepository;

    /**
     * Activates a coupon.
     * 
     * @param code Coupon code
     * @return Activated coupon
     * @throws ResourceNotFoundException if not found
     */
    public Coupon activate(String code) {
        Objects.requireNonNull(code, "Coupon code must not be null");
        Coupon coupon = findByCode(code);
        coupon.activate();
        log.info("Activated coupon {}", code);
        return couponRepository.save(coupon);
    }

    /**
     * Deactivates a coupon.
     * 
     * @param code Coupon code
     * @return Deactivated coupon
     * @throws ResourceNotFoundException if not found
     */
    public Coupon deactivate(String code) {
        Objects.requireNonNull(code, "Coupon code must not be null");
        Coupon coupon = findByCode(code);
        coupon.deactivate();
        log.info("Deactivated coupon {}", code);
        return couponRepository.save(coupon);
    }

    /**
     * Updates coupon validity period.
     * 
     * @param code      Coupon code
     * @param startDate New start date
     * @param endDate   New end date
     * @return Updated coupon
     * @throws ResourceNotFoundException if not found
     */
    public Coupon updateValidityPeriod(String code, Instant startDate, Instant endDate) {
        Objects.requireNonNull(code, "Coupon code must not be null");
        Coupon coupon = findByCode(code);
        coupon.updateValidityPeriod(startDate, endDate);
        log.info("Updated validity period for coupon {} from {} to {}", code, startDate, endDate);
        return couponRepository.save(coupon);
    }

    /**
     * Reverts coupon usage (e.g., when order is cancelled).
     * 
     * @param code Coupon code
     * @throws ResourceNotFoundException if not found
     */
    public void revertUsage(String code) {
        Objects.requireNonNull(code, "Coupon code must not be null");
        Coupon coupon = findByCode(code);
        coupon.revertUsage();
        couponRepository.save(coupon);
        log.info("Reverted usage for coupon {}", code);
    }

    /**
     * Finds a coupon by code.
     * 
     * @param code Coupon code
     * @return The coupon
     * @throws ResourceNotFoundException if not found
     */
    private Coupon findByCode(String code) {
        return couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "code", code));
    }
}
