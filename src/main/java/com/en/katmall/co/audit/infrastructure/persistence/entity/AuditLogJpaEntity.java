/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.audit.infrastructure.persistence.entity;

import com.en.katmall.co.shared.enums.KTypeActorType;
import com.en.katmall.co.shared.enums.KTypeAuditAction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * JPA Entity for Audit Log persistence.
 * Maps to 'audit_logs' table in the database.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_logs_table_record", columnList = "table_name, record_id"),
        @Index(name = "idx_audit_logs_created_at", columnList = "created_at"),
        @Index(name = "idx_audit_logs_actor", columnList = "actor_id, actor_type")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogJpaEntity {

    @Id
    @Column(length = 255)
    private String id;

    @Column(name = "table_name", nullable = false, length = 50)
    private String tableName;

    @Column(name = "record_id", nullable = false, length = 255)
    private String recordId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private KTypeAuditAction action;

    @Column(name = "actor_id", length = 255)
    private String actorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_type", length = 20)
    private KTypeActorType actorType;

    @Column(name = "old_data_text", columnDefinition = "TEXT")
    private String oldDataText;

    @Column(name = "new_data_text", columnDefinition = "TEXT")
    private String newDataText;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}
