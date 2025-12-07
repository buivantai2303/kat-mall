/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.ordering.domain.model.valueobject;

/**
 * Enumeration of order lifecycle states.
 * Provides state transition validation.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public enum OrderStatus {
    /** Order created but not yet confirmed */
    PENDING,
    /** Order confirmed by customer/admin */
    CONFIRMED,
    /** Order being prepared for shipment */
    PROCESSING,
    /** Order shipped to customer */
    SHIPPED,
    /** Order received by customer */
    DELIVERED,
    /** Order cancelled */
    CANCELLED,
    /** Order refunded */
    REFUNDED;

    /**
     * Validates if a transition to target status is allowed
     * 
     * @param target The target status
     * @return true if transition is valid
     */
    public boolean canTransitionTo(OrderStatus target) {
        return switch (this) {
            case PENDING -> target == CONFIRMED || target == CANCELLED;
            case CONFIRMED -> target == PROCESSING || target == CANCELLED;
            case PROCESSING -> target == SHIPPED || target == CANCELLED;
            case SHIPPED -> target == DELIVERED;
            case DELIVERED -> target == REFUNDED;
            case CANCELLED, REFUNDED -> false;
        };
    }
}
