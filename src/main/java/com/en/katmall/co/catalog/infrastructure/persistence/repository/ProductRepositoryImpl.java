/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.catalog.infrastructure.persistence.repository;

import com.en.katmall.co.catalog.domain.model.Product;
import com.en.katmall.co.catalog.domain.model.valueobject.Slug;
import com.en.katmall.co.catalog.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of ProductRepository.
 * TODO: Replace with JPA implementation
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final Map<String, Product> storage = new ConcurrentHashMap<>();

    @Override
    public Product save(Product product) {
        Objects.requireNonNull(product, "Product must not be null");
        storage.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Product> findBySlug(Slug slug) {
        return storage.values().stream()
                .filter(p -> p.getSlug() != null && p.getSlug().equals(slug))
                .findFirst();
    }

    @Override
    public List<Product> findByCategoryId(String categoryId) {
        return storage.values().stream()
                .filter(p -> categoryId.equals(p.getCategoryId()))
                .toList();
    }

    @Override
    public List<Product> findByBrandId(String brandId) {
        return storage.values().stream()
                .filter(p -> brandId.equals(p.getBrandId()))
                .toList();
    }

    @Override
    public List<Product> findAllActive() {
        return storage.values().stream()
                .filter(p -> !p.isDeleted())
                .toList();
    }

    @Override
    public boolean existsBySlug(Slug slug) {
        return storage.values().stream()
                .anyMatch(p -> p.getSlug() != null && p.getSlug().equals(slug));
    }

    @Override
    public void delete(Product product) {
        storage.remove(product.getId());
    }
}
