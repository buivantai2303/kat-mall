/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.catalog.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Response DTO for product information.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    /** Product unique identifier */
    private String id;

    /** Product name */
    private String name;

    /** Product slug for URL */
    private String slug;

    /** Product description */
    private String description;

    /** Current price */
    private BigDecimal price;

    /** Original price */
    private BigDecimal originalPrice;

    /** Discount percentage */
    private int discountPercent;

    /** Currency code */
    private String currency;

    /** Category information */
    private CategoryInfo category;

    /** Brand name */
    private String brand;

    /** SKU code */
    private String sku;

    /** Available stock quantity */
    private int stockQuantity;

    /** Whether product is in stock */
    private boolean inStock;

    /** Product weight in grams */
    private int weight;

    /** Product images */
    private List<String> imageUrls;

    /** Product tags */
    private List<String> tags;

    /** Product status */
    private String status;

    /** Average rating */
    private double rating;

    /** Number of reviews */
    private int reviewCount;

    /** Number of times sold */
    private int soldCount;

    /** Creation timestamp */
    private Instant createdAt;

    /** Last update timestamp */
    private Instant updatedAt;

    /**
     * Nested DTO for category info
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfo {
        private String id;
        private String name;
        private String slug;
    }
}
