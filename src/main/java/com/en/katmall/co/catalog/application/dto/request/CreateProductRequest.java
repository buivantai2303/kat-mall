/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.catalog.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request DTO for creating a new product.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    /** Product name */
    @NotBlank(message = "{validation.required}")
    @Size(min = 3, max = 255, message = "{validation.min.length}")
    private String name;

    /** Product description */
    @Size(max = 5000, message = "{validation.max.length}")
    private String description;

    /** Product price */
    @NotNull(message = "{validation.required}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{validation.number.min}")
    private BigDecimal price;

    /** Original price (for showing discount) */
    @DecimalMin(value = "0.0", message = "{validation.number.min}")
    private BigDecimal originalPrice;

    /** Category ID */
    @NotBlank(message = "{validation.required}")
    private String categoryId;

    /** Brand name */
    @Size(max = 100, message = "{validation.max.length}")
    private String brand;

    /** SKU code */
    @Size(max = 50, message = "{validation.max.length}")
    private String sku;

    /** Initial stock quantity */
    @Min(value = 0, message = "{validation.number.min}")
    private int stockQuantity;

    /** Product weight in grams */
    @Min(value = 0, message = "{validation.number.min}")
    private int weight;

    /** Product images URLs */
    private List<String> imageUrls;

    /** Product tags */
    private List<String> tags;
}
