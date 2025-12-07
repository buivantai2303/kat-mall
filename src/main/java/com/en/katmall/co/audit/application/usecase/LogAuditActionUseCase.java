/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.audit.application.usecase;

import com.en.katmall.co.audit.domain.model.AuditLog;
import com.en.katmall.co.audit.domain.repository.AuditLogRepository;
import com.en.katmall.co.shared.enums.KTypeActorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case: Log Audit Action
 * Creates audit logs for CRUD actions asynchronously.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LogAuditActionUseCase {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    /**
     * Logs a CREATE action asynchronously.
     * 
     * @param tableName Table name
     * @param recordId  Record ID
     * @param newEntity The created entity
     * @param actorId   Actor ID
     * @param actorType Actor type
     * @param ipAddress IP address
     */
    @Async
    public void logCreate(String tableName, String recordId, Object newEntity,
            String actorId, KTypeActorType actorType, String ipAddress) {
        try {
            String newData = serializeEntity(newEntity);
            AuditLog auditLog = AuditLog.create(tableName, recordId, newData, actorId, actorType, ipAddress);
            auditLogRepository.save(auditLog);
            log.debug("Logged CREATE action for {}/{}", tableName, recordId);
        } catch (Exception e) {
            log.error("Failed to log CREATE action for {}/{}: {}", tableName, recordId, e.getMessage());
        }
    }

    /**
     * Logs an UPDATE action asynchronously.
     * 
     * @param tableName Table name
     * @param recordId  Record ID
     * @param oldEntity The old entity state
     * @param newEntity The new entity state
     * @param actorId   Actor ID
     * @param actorType Actor type
     * @param ipAddress IP address
     */
    @Async
    public void logUpdate(String tableName, String recordId, Object oldEntity, Object newEntity,
            String actorId, KTypeActorType actorType, String ipAddress) {
        try {
            String oldData = serializeEntity(oldEntity);
            String newData = serializeEntity(newEntity);
            AuditLog auditLog = AuditLog.update(tableName, recordId, oldData, newData, actorId, actorType, ipAddress);
            auditLogRepository.save(auditLog);
            log.debug("Logged UPDATE action for {}/{}", tableName, recordId);
        } catch (Exception e) {
            log.error("Failed to log UPDATE action for {}/{}: {}", tableName, recordId, e.getMessage());
        }
    }

    /**
     * Logs a DELETE action asynchronously.
     * 
     * @param tableName Table name
     * @param recordId  Record ID
     * @param oldEntity The deleted entity
     * @param actorId   Actor ID
     * @param actorType Actor type
     * @param ipAddress IP address
     */
    @Async
    public void logDelete(String tableName, String recordId, Object oldEntity,
            String actorId, KTypeActorType actorType, String ipAddress) {
        try {
            String oldData = serializeEntity(oldEntity);
            AuditLog auditLog = AuditLog.delete(tableName, recordId, oldData, actorId, actorType, ipAddress);
            auditLogRepository.save(auditLog);
            log.debug("Logged DELETE action for {}/{}", tableName, recordId);
        } catch (Exception e) {
            log.error("Failed to log DELETE action for {}/{}: {}", tableName, recordId, e.getMessage());
        }
    }

    /**
     * Serializes an entity to JSON string.
     * 
     * @param entity Entity to serialize
     * @return JSON string or toString() on failure
     */
    private String serializeEntity(Object entity) {
        if (entity == null)
            return null;
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize entity: {}", e.getMessage());
            return entity.toString();
        }
    }
}
