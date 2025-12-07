/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.catalog.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request DTO for updating a product.
 * All fields are optional - only provided fields will be updated.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    /** Product name */
    @Size(min = 3, max = 255, message = "{validation.min.length}")
    private String name;

    /** Product description */
    @Size(max = 5000, message = "{validation.max.length}")
    private String description;

    /** Product price */
    @DecimalMin(value = "0.0", inclusive = false, message = "{validation.number.min}")
    private BigDecimal price;

    /** Original price */
    @DecimalMin(value = "0.0", message = "{validation.number.min}")
    private BigDecimal originalPrice;

    /** Category ID */
    private String categoryId;

    /** Brand name */
    @Size(max = 100, message = "{validation.max.length}")
    private String brand;

    /** Product status (ACTIVE, INACTIVE, DRAFT) */
    private String status;

    /** Stock quantity */
    @Min(value = 0, message = "{validation.number.min}")
    private Integer stockQuantity;

    /** Product weight in grams */
    @Min(value = 0, message = "{validation.number.min}")
    private Integer weight;

    /** Product images URLs */
    private List<String> imageUrls;

    /** Product tags */
    private List<String> tags;
}
