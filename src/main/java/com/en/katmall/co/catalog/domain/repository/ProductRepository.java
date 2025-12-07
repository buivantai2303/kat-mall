/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.catalog.domain.repository;

import com.en.katmall.co.catalog.domain.model.Product;
import com.en.katmall.co.catalog.domain.model.valueobject.Slug;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product aggregate root.
 * Defines the contract for product persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface ProductRepository {

    /**
     * Saves a product entity
     * 
     * @param product The product to save
     * @return The saved product
     */
    Product save(Product product);

    /**
     * Finds a product by ID
     * 
     * @param id The product ID
     * @return Optional containing the product if found
     */
    Optional<Product> findById(String id);

    /**
     * Finds a product by its URL slug
     * 
     * @param slug The product slug
     * @return Optional containing the product if found
     */
    Optional<Product> findBySlug(Slug slug);

    /**
     * Finds all products in a category
     * 
     * @param categoryId The category ID
     * @return List of products in the category
     */
    List<Product> findByCategoryId(String categoryId);

    /**
     * Finds all products of a brand
     * 
     * @param brandId The brand ID
     * @return List of products with the brand
     */
    List<Product> findByBrandId(String brandId);

    /**
     * Finds all active (non-deleted) products
     * 
     * @return List of active products
     */
    List<Product> findAllActive();

    /**
     * Checks if a product exists with the given slug
     * 
     * @param slug The slug to check
     * @return true if exists
     */
    boolean existsBySlug(Slug slug);

    /**
     * Deletes a product
     * 
     * @param product The product to delete
     */
    void delete(Product product);
}
