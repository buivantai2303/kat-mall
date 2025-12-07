/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

/**
 * Abstract base class for all domain entities.
 * Provides common properties like id, createdAt, and updatedAt timestamps.
 * 
 * @param <ID> The type of the entity identifier
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public abstract class BaseEntity<ID> {

    protected ID id;
    protected Instant createdAt;
    /**
     * Sets the last update timestamp
     *
     * @param updatedAt The new update timestamp
     */
    @Setter
    protected Instant updatedAt;

    /**
     * Default constructor for JPA and serialization
     */
    protected BaseEntity() {
    }

    /**
     * Constructor with identifier
     * 
     * @param id The unique identifier for this entity
     */
    protected BaseEntity(ID id) {
        this.id = id;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Marks this entity as updated by setting updatedAt to current time
     */
    public void markAsUpdated() {
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
