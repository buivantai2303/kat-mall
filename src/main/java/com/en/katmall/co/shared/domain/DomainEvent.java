/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Abstract base class for all domain events.
 * Domain events represent something significant that happened in the domain.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public abstract class DomainEvent {

    private final String eventId;
    private final Instant occurredAt;

    /**
     * Default constructor that generates unique event ID and sets occurrence time
     */
    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
    }

    /**
     * Gets the type name of this event for routing and logging
     * 
     * @return The event type name (e.g., "USER_REGISTERED", "ORDER_CREATED")
     */
    public abstract String getEventType();
}
