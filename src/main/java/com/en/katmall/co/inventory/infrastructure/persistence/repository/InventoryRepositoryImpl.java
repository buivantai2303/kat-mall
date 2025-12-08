/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.inventory.infrastructure.persistence.repository;

import com.en.katmall.co.inventory.domain.model.InventoryStock;
import com.en.katmall.co.inventory.domain.repository.InventoryRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of InventoryRepository.
 * TODO: Replace with JPA implementation
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public class InventoryRepositoryImpl implements InventoryRepository {

    private final Map<String, InventoryStock> storage = new ConcurrentHashMap<>();

    @Override
    public InventoryStock save(InventoryStock stock) {
        Objects.requireNonNull(stock, "InventoryStock must not be null");
        storage.put(stock.getId(), stock);
        return stock;
    }

    @Override
    public Optional<InventoryStock> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<InventoryStock> findByLocationAndVariant(String locationId, String variantId) {
        return storage.values().stream()
                .filter(s -> locationId.equals(s.getLocationId()) && variantId.equals(s.getVariantId()))
                .findFirst();
    }

    @Override
    public List<InventoryStock> findByVariantId(String variantId) {
        return storage.values().stream()
                .filter(s -> variantId.equals(s.getVariantId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryStock> findByLocationId(String locationId) {
        return storage.values().stream()
                .filter(s -> locationId.equals(s.getLocationId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryStock> findLowStock() {
        return storage.values().stream()
                .filter(InventoryStock::isLowStock)
                .collect(Collectors.toList());
    }
}
