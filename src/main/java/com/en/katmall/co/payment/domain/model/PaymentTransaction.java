/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.domain.model;

import com.en.katmall.co.shared.domain.BaseEntity;
import com.en.katmall.co.shared.enums.KTypePaymentStatus;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

/**
 * Payment transaction entity tracking gateway interactions.
 * Records each interaction with payment gateway for audit trail.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class PaymentTransaction extends BaseEntity<String> {

    private String paymentId;
    private String gatewayTransactionId;
    private String gatewayResponseCode;
    private String rawResponseText;
    private KTypePaymentStatus status;
    private Instant performedAt;

    /** Default constructor for JPA */
    protected PaymentTransaction() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private PaymentTransaction(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.paymentId = Objects.requireNonNull(builder.paymentId, "paymentId must not be null");
        this.gatewayTransactionId = builder.gatewayTransactionId;
        this.gatewayResponseCode = builder.gatewayResponseCode;
        this.rawResponseText = builder.rawResponseText;
        this.status = builder.status != null ? builder.status : KTypePaymentStatus.PENDING;
        this.performedAt = Instant.now();
    }

    /**
     * Creates a new builder for PaymentTransaction
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Checks if this transaction was successful
     * 
     * @return true if status is COMPLETED
     */
    public boolean isSuccessful() {
        return status == KTypePaymentStatus.COMPLETED;
    }

    /**
     * Builder class for PaymentTransaction
     */
    public static class Builder {
        private String id;
        private String paymentId;
        private String gatewayTransactionId;
        private String gatewayResponseCode;
        private String rawResponseText;
        private KTypePaymentStatus status;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder paymentId(String paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder gatewayTransactionId(String gatewayTransactionId) {
            this.gatewayTransactionId = gatewayTransactionId;
            return this;
        }

        public Builder gatewayResponseCode(String gatewayResponseCode) {
            this.gatewayResponseCode = gatewayResponseCode;
            return this;
        }

        public Builder rawResponseText(String rawResponseText) {
            this.rawResponseText = rawResponseText;
            return this;
        }

        public Builder status(KTypePaymentStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Builds the PaymentTransaction instance
         * 
         * @return New PaymentTransaction instance
         * @throws NullPointerException if required fields are missing
         */
        public PaymentTransaction build() {
            return new PaymentTransaction(this);
        }
    }
}
