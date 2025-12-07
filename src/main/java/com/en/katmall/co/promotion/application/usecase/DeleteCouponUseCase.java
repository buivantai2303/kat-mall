/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.promotion.application.usecase;

import com.en.katmall.co.promotion.domain.repository.CouponRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Delete Coupon
 * Deletes a coupon by code.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeleteCouponUseCase {

    private final CouponRepository couponRepository;

    /**
     * Executes the delete coupon use case.
     * 
     * @param code Coupon code to delete
     * @throws ResourceNotFoundException if coupon not found
     */
    public void execute(String code) {
        Objects.requireNonNull(code, "Coupon code must not be null");

        if (!couponRepository.existsByCode(code.toUpperCase())) {
            throw new ResourceNotFoundException("Coupon", "code", code);
        }

        couponRepository.deleteByCode(code.toUpperCase());
        log.info("Deleted coupon {}", code);
    }
}
