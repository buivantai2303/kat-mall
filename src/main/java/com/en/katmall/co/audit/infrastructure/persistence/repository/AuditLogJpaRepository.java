/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.audit.infrastructure.persistence.repository;

import com.en.katmall.co.audit.infrastructure.persistence.entity.AuditLogJpaEntity;
import com.en.katmall.co.shared.enums.KTypeAuditAction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA Repository for AuditLogJpaEntity.
 * Provides database access for audit log operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public interface AuditLogJpaRepository extends JpaRepository<AuditLogJpaEntity, String> {

    /**
     * Finds audit logs by table name and record ID
     * 
     * @param tableName Table name
     * @param recordId  Record ID
     * @return List of audit logs
     */
    List<AuditLogJpaEntity> findByTableNameAndRecordIdOrderByCreatedAtDesc(String tableName, String recordId);

    /**
     * Finds audit logs by actor ID and type
     * 
     * @param actorId   Actor ID
     * @param actorType Actor type as string
     * @param pageable  Pagination info
     * @return List of audit logs
     */
    @Query("SELECT a FROM AuditLogJpaEntity a WHERE a.actorId = :actorId AND a.actorType = :actorType ORDER BY a.createdAt DESC")
    List<AuditLogJpaEntity> findByActorIdAndActorType(@Param("actorId") String actorId,
            @Param("actorType") String actorType,
            Pageable pageable);

    /**
     * Finds audit logs by action type
     * 
     * @param action   Action type
     * @param pageable Pagination info
     * @return List of audit logs
     */
    List<AuditLogJpaEntity> findByActionOrderByCreatedAtDesc(KTypeAuditAction action, Pageable pageable);

    /**
     * Finds audit logs within a date range
     * 
     * @param startDate Start date
     * @param endDate   End date
     * @param pageable  Pagination info
     * @return List of audit logs
     */
    @Query("SELECT a FROM AuditLogJpaEntity a WHERE a.createdAt >= :startDate AND a.createdAt <= :endDate ORDER BY a.createdAt DESC")
    List<AuditLogJpaEntity> findByDateRange(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            Pageable pageable);

    /**
     * Finds recent audit logs
     * 
     * @param pageable Pagination info with limit
     * @return List of recent audit logs
     */
    @Query("SELECT a FROM AuditLogJpaEntity a ORDER BY a.createdAt DESC")
    List<AuditLogJpaEntity> findRecent(Pageable pageable);

    /**
     * Deletes audit logs older than specified date
     * 
     * @param before Date threshold
     * @return Number of deleted records
     */
    @Modifying
    @Query("DELETE FROM AuditLogJpaEntity a WHERE a.createdAt < :before")
    int deleteByCreatedAtBefore(@Param("before") Instant before);
}
