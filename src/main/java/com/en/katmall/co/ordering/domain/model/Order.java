/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.ordering.domain.model;

import com.en.katmall.co.ordering.domain.model.valueobject.Address;
import com.en.katmall.co.ordering.domain.model.valueobject.OrderNumber;
import com.en.katmall.co.ordering.domain.model.valueobject.OrderStatus;
import com.en.katmall.co.ordering.domain.event.OrderCreatedEvent;
import com.en.katmall.co.shared.domain.AggregateRoot;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.utils.IdGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Order aggregate root representing a customer purchase.
 * Manages order lifecycle through state transitions.
 * Contains order items, pricing, and shipping information.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public class Order extends AggregateRoot<String> {

    private OrderNumber orderNumber;
    private String userId;
    private String purchaserSnapshotId;
    private Address shippingAddress;
    private Address billingAddress;
    private OrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal shippingTotal;
    private BigDecimal taxTotal;
    private BigDecimal discountTotal;
    private BigDecimal grandTotal;
    private String note;

    private List<OrderItem> items = new ArrayList<>();

    /**
     * Default constructor for JPA
     */
    protected Order() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     * 
     * @param builder The builder instance
     */
    private Order(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.orderNumber = OrderNumber.generate();
        this.userId = builder.userId;
        this.purchaserSnapshotId = builder.purchaserSnapshotId;
        this.shippingAddress = builder.shippingAddress;
        this.billingAddress = builder.billingAddress;
        this.status = OrderStatus.PENDING;
        this.subtotal = builder.subtotal;
        this.shippingTotal = builder.shippingTotal != null ? builder.shippingTotal : BigDecimal.ZERO;
        this.taxTotal = builder.taxTotal != null ? builder.taxTotal : BigDecimal.ZERO;
        this.discountTotal = builder.discountTotal != null ? builder.discountTotal : BigDecimal.ZERO;
        this.grandTotal = calculateGrandTotal();
        this.note = builder.note;
        this.items = builder.items != null ? builder.items : new ArrayList<>();

        // Register order created event
        registerEvent(new OrderCreatedEvent(this.id, this.orderNumber.getValue(), this.userId));
    }

    /**
     * Creates a new builder for Order
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Calculates the grand total from subtotal, shipping, tax, and discount
     * 
     * @return The calculated grand total
     */
    private BigDecimal calculateGrandTotal() {
        return subtotal.add(shippingTotal).add(taxTotal).subtract(discountTotal);
    }

    /**
     * Confirms the order (from PENDING to CONFIRMED)
     * 
     * @throws DomainException if order is not in PENDING status
     */
    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new DomainException("ORDER_INVALID_STATUS", "Order can only be confirmed from PENDING status");
        }
        this.status = OrderStatus.CONFIRMED;
        markAsUpdated();
    }

    /**
     * Starts processing the order (from CONFIRMED to PROCESSING)
     * 
     * @throws DomainException if order is not in CONFIRMED status
     */
    public void process() {
        if (status != OrderStatus.CONFIRMED) {
            throw new DomainException("ORDER_INVALID_STATUS", "Order can only be processed from CONFIRMED status");
        }
        this.status = OrderStatus.PROCESSING;
        markAsUpdated();
    }

    /**
     * Ships the order (from PROCESSING to SHIPPED)
     * 
     * @throws DomainException if order is not in PROCESSING status
     */
    public void ship() {
        if (status != OrderStatus.PROCESSING) {
            throw new DomainException("ORDER_INVALID_STATUS", "Order can only be shipped from PROCESSING status");
        }
        this.status = OrderStatus.SHIPPED;
        markAsUpdated();
    }

    /**
     * Marks order as delivered (from SHIPPED to DELIVERED)
     * 
     * @throws DomainException if order is not in SHIPPED status
     */
    public void deliver() {
        if (status != OrderStatus.SHIPPED) {
            throw new DomainException("ORDER_INVALID_STATUS", "Order can only be delivered from SHIPPED status");
        }
        this.status = OrderStatus.DELIVERED;
        markAsUpdated();
    }

    /**
     * Cancels the order
     * 
     * @param reason The cancellation reason
     * @throws DomainException if order has been shipped or delivered
     */
    public void cancel(String reason) {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED) {
            throw new DomainException("ORDER_CANNOT_CANCEL", "Cannot cancel order in current status");
        }
        this.status = OrderStatus.CANCELLED;
        this.note = reason;
        markAsUpdated();
    }

    // Getters
    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getPurchaserSnapshotId() {
        return purchaserSnapshotId;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getShippingTotal() {
        return shippingTotal;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public String getNote() {
        return note;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Builder class for Order
     */
    public static class Builder {
        private String id;
        private String userId;
        private String purchaserSnapshotId;
        private Address shippingAddress;
        private Address billingAddress;
        private BigDecimal subtotal;
        private BigDecimal shippingTotal;
        private BigDecimal taxTotal;
        private BigDecimal discountTotal;
        private String note;
        private List<OrderItem> items;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder purchaserSnapshotId(String id) {
            this.purchaserSnapshotId = id;
            return this;
        }

        public Builder shippingAddress(Address address) {
            this.shippingAddress = address;
            return this;
        }

        public Builder billingAddress(Address address) {
            this.billingAddress = address;
            return this;
        }

        public Builder subtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
            return this;
        }

        public Builder shippingTotal(BigDecimal shippingTotal) {
            this.shippingTotal = shippingTotal;
            return this;
        }

        public Builder taxTotal(BigDecimal taxTotal) {
            this.taxTotal = taxTotal;
            return this;
        }

        public Builder discountTotal(BigDecimal discountTotal) {
            this.discountTotal = discountTotal;
            return this;
        }

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public Builder items(List<OrderItem> items) {
            this.items = items;
            return this;
        }

        /**
         * Builds the Order instance
         * 
         * @return New Order instance
         */
        public Order build() {
            return new Order(this);
        }
    }
}
