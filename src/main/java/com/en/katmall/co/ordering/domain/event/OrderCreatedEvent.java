/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.ordering.domain.event;

import com.en.katmall.co.shared.domain.DomainEvent;
import lombok.Getter;

/**
 * Domain event raised when a new order is created.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public class OrderCreatedEvent extends DomainEvent {

    private final String orderId;
    private final String orderNumber;
    private final String userId;

    /**
     * Creates a new order created event
     * 
     * @param orderId     The ID of the created order
     * @param orderNumber The human-readable order number
     * @param userId      The ID of the user who placed the order
     */
    public OrderCreatedEvent(String orderId, String orderNumber, String userId) {
        super();
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.userId = userId;
    }

    @Override
    public String getEventType() {
        return "ORDER_CREATED";
    }
}
