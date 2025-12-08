/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.promotion.infrastructure.persistence.repository;

import com.en.katmall.co.promotion.domain.model.Coupon;
import com.en.katmall.co.promotion.domain.repository.CouponRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of CouponRepository.
 * TODO: Replace with JPA implementation
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final Map<String, Coupon> storage = new ConcurrentHashMap<>();

    @Override
    public Coupon save(Coupon coupon) {
        Objects.requireNonNull(coupon, "Coupon must not be null");
        storage.put(coupon.getCode(), coupon);
        return coupon;
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        return Optional.ofNullable(storage.get(code));
    }

    @Override
    public List<Coupon> findAllActive() {
        return storage.values().stream()
                .filter(Coupon::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public List<Coupon> findAllValid() {
        return storage.values().stream()
                .filter(Coupon::isValid)
                .collect(Collectors.toList());
    }

    @Override
    public List<Coupon> findAll(int page, int size) {
        return storage.values().stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByCode(String code) {
        storage.remove(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return storage.containsKey(code);
    }
}
