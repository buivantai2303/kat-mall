/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.audit.application.usecase;

import com.en.katmall.co.audit.domain.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Use Case: Cleanup Old Audit Logs
 * Removes audit logs older than a specified retention period.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CleanupAuditLogsUseCase {

    private final AuditLogRepository auditLogRepository;

    /**
     * Cleans up old audit logs.
     * 
     * @param retentionDays Number of days to keep logs
     * @return Number of deleted records
     */
    public int execute(int retentionDays) {
        Instant threshold = Instant.now().minusSeconds(retentionDays * 24L * 60 * 60);
        int deleted = auditLogRepository.deleteOlderThan(threshold);
        log.info("Cleaned up {} audit logs older than {} days", deleted, retentionDays);
        return deleted;
    }
}
