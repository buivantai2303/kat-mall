/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.audit.application.usecase;

import com.en.katmall.co.audit.domain.model.AuditLog;
import com.en.katmall.co.audit.domain.repository.AuditLogRepository;
import com.en.katmall.co.shared.enums.KTypeAuditAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Use Case: Query Audit Logs
 * Retrieves audit logs with various criteria.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryAuditLogsUseCase {

    private final AuditLogRepository auditLogRepository;

    /**
     * Finds audit history for a specific record.
     * 
     * @param tableName Table name
     * @param recordId  Record ID
     * @return List of audit logs
     */
    public List<AuditLog> findRecordHistory(String tableName, String recordId) {
        return auditLogRepository.findByTableAndRecord(tableName, recordId);
    }

    /**
     * Finds audit logs by actor.
     * 
     * @param actorId   Actor ID
     * @param actorType Actor type
     * @param page      Page number
     * @param size      Page size
     * @return List of audit logs
     */
    public List<AuditLog> findByActor(String actorId, String actorType, int page, int size) {
        return auditLogRepository.findByActor(actorId, actorType, page, size);
    }

    /**
     * Finds audit logs by action type.
     * 
     * @param action Action type
     * @param page   Page number
     * @param size   Page size
     * @return List of audit logs
     */
    public List<AuditLog> findByAction(KTypeAuditAction action, int page, int size) {
        return auditLogRepository.findByAction(action, page, size);
    }

    /**
     * Finds audit logs within a date range.
     * 
     * @param startDate Start date
     * @param endDate   End date
     * @param page      Page number
     * @param size      Page size
     * @return List of audit logs
     */
    public List<AuditLog> findByDateRange(Instant startDate, Instant endDate, int page, int size) {
        return auditLogRepository.findByDateRange(startDate, endDate, page, size);
    }

    /**
     * Finds recent audit logs.
     * 
     * @param limit Maximum number of logs
     * @return List of recent audit logs
     */
    public List<AuditLog> findRecent(int limit) {
        return auditLogRepository.findRecent(limit);
    }
}
