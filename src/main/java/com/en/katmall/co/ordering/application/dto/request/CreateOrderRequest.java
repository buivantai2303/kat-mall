/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.ordering.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for creating a new order.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    /** List of order items */
    @NotEmpty(message = "{validation.required}")
    @Valid
    private List<OrderItemRequest> items;

    /** Shipping address */
    @NotNull(message = "{validation.required}")
    @Valid
    private ShippingAddressRequest shippingAddress;

    /** Payment method code */
    @NotBlank(message = "{validation.required}")
    private String paymentMethod;

    /** Shipping method code */
    @NotBlank(message = "{validation.required}")
    private String shippingMethod;

    /** Coupon code (optional) */
    @Size(max = 50, message = "{validation.max.length}")
    private String couponCode;

    /** Order note (optional) */
    @Size(max = 500, message = "{validation.max.length}")
    private String note;

    /**
     * Order item request
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {

        @NotBlank(message = "{validation.required}")
        private String productId;

        private String variantId;

        @Min(value = 1, message = "{validation.number.min}")
        private int quantity;
    }

    /**
     * Shipping address request
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingAddressRequest {

        @NotBlank(message = "{validation.required}")
        @Size(max = 100, message = "{validation.max.length}")
        private String recipientName;

        @NotBlank(message = "{validation.required}")
        @Size(max = 20, message = "{validation.max.length}")
        private String phone;

        @NotBlank(message = "{validation.required}")
        @Size(max = 255, message = "{validation.max.length}")
        private String streetAddress;

        @NotBlank(message = "{validation.required}")
        private String ward;

        @NotBlank(message = "{validation.required}")
        private String district;

        @NotBlank(message = "{validation.required}")
        private String city;
    }
}
