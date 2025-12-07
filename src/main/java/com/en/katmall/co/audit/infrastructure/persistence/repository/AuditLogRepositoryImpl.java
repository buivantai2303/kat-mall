/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.audit.infrastructure.persistence.repository;

import com.en.katmall.co.audit.domain.model.AuditLog;
import com.en.katmall.co.audit.domain.repository.AuditLogRepository;
import com.en.katmall.co.audit.infrastructure.persistence.entity.AuditLogJpaEntity;
import com.en.katmall.co.audit.infrastructure.persistence.mapper.AuditLogPersistenceMapper;
import com.en.katmall.co.shared.enums.KTypeAuditAction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of AuditLogRepository using Spring Data JPA.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class AuditLogRepositoryImpl implements AuditLogRepository {

    private final AuditLogJpaRepository jpaRepository;
    private final AuditLogPersistenceMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AuditLog save(AuditLog auditLog) {
        Objects.requireNonNull(auditLog, "AuditLog must not be null");

        AuditLogJpaEntity entity = mapper.toEntity(auditLog);
        AuditLogJpaEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AuditLog> findById(String id) {
        Objects.requireNonNull(id, "ID must not be null");

        return jpaRepository.findById(id)
                .map(mapper::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByTableAndRecord(String tableName, String recordId) {
        Objects.requireNonNull(tableName, "Table name must not be null");
        Objects.requireNonNull(recordId, "Record ID must not be null");

        return jpaRepository.findByTableNameAndRecordIdOrderByCreatedAtDesc(tableName, recordId)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByActor(String actorId, String actorType, int page, int size) {
        Objects.requireNonNull(actorId, "Actor ID must not be null");
        Objects.requireNonNull(actorType, "Actor type must not be null");

        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByActorIdAndActorType(actorId, actorType, pageable)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByAction(KTypeAuditAction action, int page, int size) {
        Objects.requireNonNull(action, "Action must not be null");

        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByActionOrderByCreatedAtDesc(action, pageable)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByDateRange(Instant startDate, Instant endDate, int page, int size) {
        Objects.requireNonNull(startDate, "Start date must not be null");
        Objects.requireNonNull(endDate, "End date must not be null");

        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByDateRange(startDate, endDate, pageable)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findRecent(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return jpaRepository.findRecent(pageable)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int deleteOlderThan(Instant before) {
        Objects.requireNonNull(before, "Before date must not be null");

        return jpaRepository.deleteByCreatedAtBefore(before);
    }
}
