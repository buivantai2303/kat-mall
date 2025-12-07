/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.inventory.domain.repository;

import com.en.katmall.co.inventory.domain.model.InventoryStock;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for InventoryStock aggregate root.
 * Defines the contract for inventory persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface InventoryRepository {

    /**
     * Saves an inventory stock entity
     * 
     * @param stock The stock to save
     * @return The saved stock
     */
    InventoryStock save(InventoryStock stock);

    /**
     * Finds inventory stock by ID
     * 
     * @param id The stock ID
     * @return Optional containing the stock if found
     */
    Optional<InventoryStock> findById(String id);

    /**
     * Finds inventory stock by location and variant
     * 
     * @param locationId The warehouse location ID
     * @param variantId  The product variant ID
     * @return Optional containing the stock if found
     */
    Optional<InventoryStock> findByLocationAndVariant(String locationId, String variantId);

    /**
     * Finds all inventory records for a variant across locations
     * 
     * @param variantId The product variant ID
     * @return List of inventory stocks
     */
    List<InventoryStock> findByVariantId(String variantId);

    /**
     * Finds all inventory records at a location
     * 
     * @param locationId The warehouse location ID
     * @return List of inventory stocks
     */
    List<InventoryStock> findByLocationId(String locationId);

    /**
     * Finds all items with low stock (below threshold)
     * 
     * @return List of low stock inventory items
     */
    List<InventoryStock> findLowStock();
}
