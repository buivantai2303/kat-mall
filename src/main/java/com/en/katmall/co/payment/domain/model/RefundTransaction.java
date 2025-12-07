/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.domain.model;

import com.en.katmall.co.shared.domain.BaseEntity;
import com.en.katmall.co.shared.enums.KTypeRefundStatus;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Refund transaction entity for handling payment refunds.
 * Tracks refund processing through payment gateway.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class RefundTransaction extends BaseEntity<String> {

    // Error codes
    private static final String ERR_INVALID_STATUS_TRANSITION = "INVALID_STATUS_TRANSITION";

    private String paymentTransactionId;
    private BigDecimal refundAmount;
    private KTypeRefundStatus status;
    private String gatewayRefundId;

    /** Default constructor for JPA */
    protected RefundTransaction() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private RefundTransaction(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.paymentTransactionId = Objects.requireNonNull(builder.paymentTransactionId,
                "paymentTransactionId must not be null");
        this.refundAmount = Objects.requireNonNull(builder.refundAmount, "refundAmount must not be null");
        this.status = KTypeRefundStatus.PENDING;
        this.gatewayRefundId = null;
    }

    /**
     * Creates a new builder for RefundTransaction
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Starts processing the refund
     */
    public void startProcessing() {
        if (status != KTypeRefundStatus.PENDING) {
            throw new DomainException(ERR_INVALID_STATUS_TRANSITION,
                    "Can only start processing from PENDING status");
        }
        this.status = KTypeRefundStatus.PROCESSING;
        markAsUpdated();
    }

    /**
     * Marks the refund as completed
     * 
     * @param gatewayRefundId Refund ID from payment gateway
     */
    public void complete(String gatewayRefundId) {
        if (status != KTypeRefundStatus.PROCESSING) {
            throw new DomainException(ERR_INVALID_STATUS_TRANSITION,
                    "Can only complete from PROCESSING status");
        }
        this.status = KTypeRefundStatus.COMPLETED;
        this.gatewayRefundId = gatewayRefundId;
        markAsUpdated();
    }

    /**
     * Marks the refund as failed
     */
    public void fail() {
        if (status != KTypeRefundStatus.PROCESSING) {
            throw new DomainException(ERR_INVALID_STATUS_TRANSITION,
                    "Can only fail from PROCESSING status");
        }
        this.status = KTypeRefundStatus.FAILED;
        markAsUpdated();
    }

    /**
     * Checks if refund is completed
     * 
     * @return true if status is COMPLETED
     */
    public boolean isCompleted() {
        return status == KTypeRefundStatus.COMPLETED;
    }

    /**
     * Builder class for RefundTransaction
     */
    public static class Builder {
        private String id;
        private String paymentTransactionId;
        private BigDecimal refundAmount;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder paymentTransactionId(String paymentTransactionId) {
            this.paymentTransactionId = paymentTransactionId;
            return this;
        }

        public Builder refundAmount(BigDecimal refundAmount) {
            this.refundAmount = refundAmount;
            return this;
        }

        /**
         * Builds the RefundTransaction instance
         * 
         * @return New RefundTransaction instance
         * @throws NullPointerException if required fields are missing
         */
        public RefundTransaction build() {
            return new RefundTransaction(this);
        }
    }
}
