/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class for Aggregate Root entities in DDD.
 * Aggregate Roots are the main entry points to aggregates and maintain
 * transactional consistency boundaries. They can register and dispatch domain
 * events.
 * 
 * @param <ID> The type of the entity identifier
 * @author tai.buivan
 * @version 1.0
 */
public abstract class AggregateRoot<ID> extends BaseEntity<ID> {

    // Collection of domain events raised by this aggregate
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * Default constructor for JPA and serialization
     */
    protected AggregateRoot() {
        super();
    }

    /**
     * Constructor with identifier
     * 
     * @param id The unique identifier for this aggregate root
     */
    protected AggregateRoot(ID id) {
        super(id);
    }

    /**
     * Registers a domain event to be dispatched later
     * 
     * @param event The domain event to register
     */
    protected void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    /**
     * Gets all registered domain events
     * 
     * @return Unmodifiable list of domain events
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Clears all registered domain events after they have been dispatched
     */
    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
