/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.inventory.domain.model;

import com.en.katmall.co.shared.domain.AggregateRoot;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Inventory stock aggregate root managing product availability.
 * Handles stock quantities, reservations, and low stock alerts.
 * Uses optimistic locking via version field.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class InventoryStock extends AggregateRoot<String> {

    // Error codes
    private static final String ERR_INVALID_QUANTITY = "INVALID_QUANTITY";
    private static final String ERR_INSUFFICIENT_STOCK = "INSUFFICIENT_STOCK";
    private static final String ERR_INVALID_RESERVATION = "INVALID_RESERVATION";

    private String locationId;
    private String variantId;
    private int quantityOnHand;
    private int quantityReserved;
    private int lowStockThreshold;
    private int version;

    /** Default constructor for JPA */
    protected InventoryStock() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private InventoryStock(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.locationId = Objects.requireNonNull(builder.locationId, "locationId must not be null");
        this.variantId = Objects.requireNonNull(builder.variantId, "variantId must not be null");
        this.quantityOnHand = Math.max(0, builder.quantityOnHand);
        this.quantityReserved = 0;
        this.lowStockThreshold = builder.lowStockThreshold > 0 ? builder.lowStockThreshold : 10;
        this.version = 0;
    }

    /**
     * Creates a new builder for InventoryStock
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets the available quantity (on hand minus reserved)
     * 
     * @return Available quantity for new orders
     */
    public int getAvailableQuantity() {
        return quantityOnHand - quantityReserved;
    }

    /**
     * Checks if stock level is below threshold
     * 
     * @return true if low stock alert should be triggered
     */
    public boolean isLowStock() {
        return getAvailableQuantity() <= lowStockThreshold;
    }

    /**
     * Checks if this stock is out of stock
     * 
     * @return true if no available quantity
     */
    public boolean isOutOfStock() {
        return getAvailableQuantity() <= 0;
    }

    /**
     * Reserves stock for an order
     * 
     * @param quantity The quantity to reserve
     * @throws DomainException if insufficient stock available
     */
    public void reserve(int quantity) {
        validatePositiveQuantity(quantity);
        if (getAvailableQuantity() < quantity) {
            throw new DomainException(ERR_INSUFFICIENT_STOCK,
                    String.format("Not enough stock available. Available: %d, Requested: %d",
                            getAvailableQuantity(), quantity));
        }
        this.quantityReserved += quantity;
        this.version++;
        markAsUpdated();
    }

    /**
     * Releases a previous reservation (e.g., order cancelled)
     * 
     * @param quantity The quantity to release
     * @throws DomainException if quantity is invalid
     */
    public void releaseReservation(int quantity) {
        validatePositiveQuantity(quantity);
        this.quantityReserved = Math.max(0, this.quantityReserved - quantity);
        this.version++;
        markAsUpdated();
    }

    /**
     * Adds stock (e.g., from receiving shipment)
     * 
     * @param quantity The quantity to add
     * @throws DomainException if quantity is invalid
     */
    public void addStock(int quantity) {
        validatePositiveQuantity(quantity);
        this.quantityOnHand += quantity;
        this.version++;
        markAsUpdated();
    }

    /**
     * Removes stock directly (e.g., inventory adjustment)
     * 
     * @param quantity The quantity to remove
     * @throws DomainException if insufficient stock
     */
    public void removeStock(int quantity) {
        validatePositiveQuantity(quantity);
        if (this.quantityOnHand < quantity) {
            throw new DomainException(ERR_INSUFFICIENT_STOCK,
                    String.format("Cannot remove %d units. Only %d on hand", quantity, quantityOnHand));
        }
        this.quantityOnHand -= quantity;
        this.version++;
        markAsUpdated();
    }

    /**
     * Confirms a sale - converts reservation to actual deduction
     * 
     * @param quantity The quantity sold
     * @throws DomainException if reservation is invalid
     */
    public void confirmSale(int quantity) {
        validatePositiveQuantity(quantity);
        if (this.quantityReserved < quantity) {
            throw new DomainException(ERR_INVALID_RESERVATION,
                    String.format("Not enough reserved. Reserved: %d, Requested: %d",
                            quantityReserved, quantity));
        }
        this.quantityReserved -= quantity;
        this.quantityOnHand -= quantity;
        this.version++;
        markAsUpdated();
    }

    /**
     * Updates the low stock threshold
     * 
     * @param threshold New threshold value
     */
    public void updateLowStockThreshold(int threshold) {
        if (threshold < 0) {
            throw new DomainException(ERR_INVALID_QUANTITY, "Threshold cannot be negative");
        }
        this.lowStockThreshold = threshold;
        markAsUpdated();
    }

    private void validatePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new DomainException(ERR_INVALID_QUANTITY, "Quantity must be positive");
        }
    }

    /**
     * Builder class for InventoryStock
     */
    public static class Builder {
        private String id;
        private String locationId;
        private String variantId;
        private int quantityOnHand;
        private int lowStockThreshold = 10;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder locationId(String locationId) {
            this.locationId = locationId;
            return this;
        }

        public Builder variantId(String variantId) {
            this.variantId = variantId;
            return this;
        }

        public Builder quantityOnHand(int quantity) {
            this.quantityOnHand = quantity;
            return this;
        }

        public Builder lowStockThreshold(int threshold) {
            this.lowStockThreshold = threshold;
            return this;
        }

        /**
         * Builds the InventoryStock instance
         * 
         * @return New InventoryStock instance
         * @throws NullPointerException if required fields are missing
         */
        public InventoryStock build() {
            return new InventoryStock(this);
        }
    }
}
