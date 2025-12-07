/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.ordering.domain.model;

import com.en.katmall.co.shared.domain.BaseEntity;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Entity representing a line item in an order.
 * Contains product snapshot data to preserve order history.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public class OrderItem extends BaseEntity<String> {

    private String orderId;
    private String itemSnapshotId;
    private String sku;
    private String productName;
    private String variantName;
    private String imageUrl;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    /**
     * Default constructor for JPA
     */
    protected OrderItem() {
        super();
    }

    /**
     * Creates an order item with all required fields
     * 
     * @param id             Unique item ID
     * @param orderId        Parent order ID
     * @param itemSnapshotId Reference to product snapshot
     * @param sku            Product SKU
     * @param productName    Product name at time of order
     * @param variantName    Variant name at time of order
     * @param imageUrl       Product image URL
     * @param quantity       Ordered quantity
     * @param unitPrice      Price per unit
     */
    public OrderItem(String id, String orderId, String itemSnapshotId, String sku,
            String productName, String variantName, String imageUrl,
            int quantity, BigDecimal unitPrice) {
        super(id);
        this.orderId = orderId;
        this.itemSnapshotId = itemSnapshotId;
        this.sku = sku;
        this.productName = productName;
        this.variantName = variantName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
