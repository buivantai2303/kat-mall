/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.audit.domain.model;

import com.en.katmall.co.shared.domain.BaseEntity;
import com.en.katmall.co.shared.enums.KTypeAuditAction;
import com.en.katmall.co.shared.enums.KTypeActorType;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Audit log entity for tracking data changes.
 * Records CREATE, UPDATE, DELETE operations on entities.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class AuditLog extends BaseEntity<String> {

    private String tableName;
    private String recordId;
    private KTypeAuditAction action;
    private String actorId;
    private KTypeActorType actorType;
    private String oldDataText;
    private String newDataText;
    private String ipAddress;

    /** Default constructor for JPA */
    protected AuditLog() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private AuditLog(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.tableName = Objects.requireNonNull(builder.tableName, "tableName must not be null");
        this.recordId = Objects.requireNonNull(builder.recordId, "recordId must not be null");
        this.action = Objects.requireNonNull(builder.action, "action must not be null");
        this.actorId = builder.actorId;
        this.actorType = builder.actorType;
        this.oldDataText = builder.oldDataText;
        this.newDataText = builder.newDataText;
        this.ipAddress = builder.ipAddress;
    }

    /**
     * Creates a new builder for AuditLog
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Factory method for CREATE action
     * 
     * @param tableName Table name
     * @param recordId  Record ID
     * @param newData   New data as text
     * @param actorId   Actor performing the action
     * @param actorType Type of actor
     * @param ipAddress IP address
     * @return New AuditLog for CREATE
     */
    public static AuditLog create(String tableName, String recordId, String newData,
            String actorId, KTypeActorType actorType, String ipAddress) {
        return builder()
                .tableName(tableName)
                .recordId(recordId)
                .action(KTypeAuditAction.CREATE)
                .newDataText(newData)
                .actorId(actorId)
                .actorType(actorType)
                .ipAddress(ipAddress)
                .build();
    }

    /**
     * Factory method for UPDATE action
     * 
     * @param tableName Table name
     * @param recordId  Record ID
     * @param oldData   Old data as text
     * @param newData   New data as text
     * @param actorId   Actor performing the action
     * @param actorType Type of actor
     * @param ipAddress IP address
     * @return New AuditLog for UPDATE
     */
    public static AuditLog update(String tableName, String recordId, String oldData, String newData,
            String actorId, KTypeActorType actorType, String ipAddress) {
        return builder()
                .tableName(tableName)
                .recordId(recordId)
                .action(KTypeAuditAction.UPDATE)
                .oldDataText(oldData)
                .newDataText(newData)
                .actorId(actorId)
                .actorType(actorType)
                .ipAddress(ipAddress)
                .build();
    }

    /**
     * Factory method for DELETE action
     * 
     * @param tableName Table name
     * @param recordId  Record ID
     * @param oldData   Old data as text
     * @param actorId   Actor performing the action
     * @param actorType Type of actor
     * @param ipAddress IP address
     * @return New AuditLog for DELETE
     */
    public static AuditLog delete(String tableName, String recordId, String oldData,
            String actorId, KTypeActorType actorType, String ipAddress) {
        return builder()
                .tableName(tableName)
                .recordId(recordId)
                .action(KTypeAuditAction.DELETE)
                .oldDataText(oldData)
                .actorId(actorId)
                .actorType(actorType)
                .ipAddress(ipAddress)
                .build();
    }

    /**
     * Builder class for AuditLog
     */
    public static class Builder {
        private String id;
        private String tableName;
        private String recordId;
        private KTypeAuditAction action;
        private String actorId;
        private KTypeActorType actorType;
        private String oldDataText;
        private String newDataText;
        private String ipAddress;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder recordId(String recordId) {
            this.recordId = recordId;
            return this;
        }

        public Builder action(KTypeAuditAction action) {
            this.action = action;
            return this;
        }

        public Builder actorId(String actorId) {
            this.actorId = actorId;
            return this;
        }

        public Builder actorType(KTypeActorType actorType) {
            this.actorType = actorType;
            return this;
        }

        public Builder oldDataText(String oldDataText) {
            this.oldDataText = oldDataText;
            return this;
        }

        public Builder newDataText(String newDataText) {
            this.newDataText = newDataText;
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        /**
         * Builds the AuditLog instance
         * 
         * @return New AuditLog instance
         * @throws NullPointerException if required fields are missing
         */
        public AuditLog build() {
            return new AuditLog(this);
        }
    }
}
