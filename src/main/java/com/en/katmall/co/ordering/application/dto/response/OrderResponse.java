/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.ordering.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Response DTO for order information.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    /** Order unique identifier */
    private String id;

    /** Order number for display */
    private String orderNumber;

    /** Order status */
    private String status;

    /** Order status display name (localized) */
    private String statusDisplay;

    /** List of order items */
    private List<OrderItemResponse> items;

    /** Subtotal before discounts */
    private BigDecimal subtotal;

    /** Discount amount */
    private BigDecimal discountAmount;

    /** Shipping fee */
    private BigDecimal shippingFee;

    /** Tax amount */
    private BigDecimal taxAmount;

    /** Total amount */
    private BigDecimal totalAmount;

    /** Currency code */
    private String currency;

    /** Shipping address */
    private AddressResponse shippingAddress;

    /** Payment method */
    private String paymentMethod;

    /** Payment status */
    private String paymentStatus;

    /** Shipping method */
    private String shippingMethod;

    /** Tracking number */
    private String trackingNumber;

    /** Order note */
    private String note;

    /** Order creation time */
    private Instant createdAt;

    /** Last update time */
    private Instant updatedAt;

    /** Estimated delivery date */
    private Instant estimatedDeliveryAt;

    /**
     * Order item response
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private String productId;
        private String productName;
        private String productImage;
        private String variantName;
        private String sku;
        private BigDecimal unitPrice;
        private int quantity;
        private BigDecimal totalPrice;
    }

    /**
     * Address response
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressResponse {
        private String recipientName;
        private String phone;
        private String streetAddress;
        private String ward;
        private String district;
        private String city;
        private String fullAddress;
    }
}
