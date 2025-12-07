/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.audit.infrastructure.persistence.mapper;

import com.en.katmall.co.audit.domain.model.AuditLog;
import com.en.katmall.co.audit.infrastructure.persistence.entity.AuditLogJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Mapper for converting between AuditLog domain model and AuditLogJpaEntity.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Component
public class AuditLogPersistenceMapper {

    /**
     * Converts domain model to JPA entity
     * 
     * @param model Domain model
     * @return JPA entity
     */
    public AuditLogJpaEntity toEntity(AuditLog model) {
        Objects.requireNonNull(model, "AuditLog model must not be null");

        return AuditLogJpaEntity.builder()
                .id(model.getId())
                .tableName(model.getTableName())
                .recordId(model.getRecordId())
                .action(model.getAction())
                .actorId(model.getActorId())
                .actorType(model.getActorType())
                .oldDataText(model.getOldDataText())
                .newDataText(model.getNewDataText())
                .ipAddress(model.getIpAddress())
                .createdAt(model.getCreatedAt())
                .build();
    }

    /**
     * Converts JPA entity to domain model
     * 
     * @param entity JPA entity
     * @return Domain model
     */
    public AuditLog toModel(AuditLogJpaEntity entity) {
        Objects.requireNonNull(entity, "AuditLogJpaEntity must not be null");

        // Note: createdAt is set automatically in BaseEntity constructor
        // and cannot be overridden from outside (no setter)
        return AuditLog.builder()
                .id(entity.getId())
                .tableName(entity.getTableName())
                .recordId(entity.getRecordId())
                .action(entity.getAction())
                .actorId(entity.getActorId())
                .actorType(entity.getActorType())
                .oldDataText(entity.getOldDataText())
                .newDataText(entity.getNewDataText())
                .ipAddress(entity.getIpAddress())
                .build();
    }
}
