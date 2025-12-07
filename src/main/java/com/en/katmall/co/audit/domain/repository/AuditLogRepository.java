/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.audit.domain.repository;

import com.en.katmall.co.audit.domain.model.AuditLog;
import com.en.katmall.co.shared.enums.KTypeAuditAction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AuditLog entity.
 * Defines the contract for audit log persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface AuditLogRepository {

    /**
     * Saves an audit log entry
     * 
     * @param auditLog The audit log to save
     * @return The saved audit log
     */
    AuditLog save(AuditLog auditLog);

    /**
     * Finds audit log by ID
     * 
     * @param id The audit log ID
     * @return Optional containing the audit log if found
     */
    Optional<AuditLog> findById(String id);

    /**
     * Finds audit logs for a specific record
     * 
     * @param tableName Table name
     * @param recordId  Record ID
     * @return List of audit logs for the record
     */
    List<AuditLog> findByTableAndRecord(String tableName, String recordId);

    /**
     * Finds audit logs by actor
     * 
     * @param actorId   Actor ID
     * @param actorType Actor type
     * @param page      Page number
     * @param size      Page size
     * @return List of audit logs by the actor
     */
    List<AuditLog> findByActor(String actorId, String actorType, int page, int size);

    /**
     * Finds audit logs by action type
     * 
     * @param action Action type
     * @param page   Page number
     * @param size   Page size
     * @return List of audit logs with the action
     */
    List<AuditLog> findByAction(KTypeAuditAction action, int page, int size);

    /**
     * Finds audit logs within a date range
     * 
     * @param startDate Start date
     * @param endDate   End date
     * @param page      Page number
     * @param size      Page size
     * @return List of audit logs within the range
     */
    List<AuditLog> findByDateRange(Instant startDate, Instant endDate, int page, int size);

    /**
     * Finds recent audit logs
     * 
     * @param limit Maximum number of logs
     * @return List of recent audit logs
     */
    List<AuditLog> findRecent(int limit);

    /**
     * Deletes audit logs older than a specified date
     * 
     * @param before Date threshold
     * @return Number of deleted records
     */
    int deleteOlderThan(Instant before);
}
