/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.domain.model;

import com.en.katmall.co.shared.domain.AggregateRoot;
import com.en.katmall.co.shared.enums.KTypePaymentMethod;
import com.en.katmall.co.shared.enums.KTypePaymentStatus;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Payment aggregate root managing payment transactions.
 * Handles payment processing, status transitions, and transaction history.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class Payment extends AggregateRoot<String> {

    // Error codes
    private static final String ERR_INVALID_AMOUNT = "INVALID_AMOUNT";
    private static final String ERR_INVALID_STATUS_TRANSITION = "INVALID_STATUS_TRANSITION";
    private static final String ERR_ALREADY_REFUNDED = "ALREADY_REFUNDED";

    private String orderId;
    private BigDecimal amount;
    private KTypePaymentMethod paymentMethod;
    private KTypePaymentStatus status;
    private final List<PaymentTransaction> transactions = new ArrayList<>();

    /** Default constructor for JPA */
    protected Payment() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private Payment(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.orderId = Objects.requireNonNull(builder.orderId, "orderId must not be null");
        this.amount = validateAmount(builder.amount);
        this.paymentMethod = Objects.requireNonNull(builder.paymentMethod, "paymentMethod must not be null");
        this.status = KTypePaymentStatus.PENDING;
    }

    /**
     * Creates a new builder for Payment
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Validates that the amount is positive
     */
    private BigDecimal validateAmount(BigDecimal amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException(ERR_INVALID_AMOUNT, "Payment amount must be positive");
        }
        return amount;
    }

    /**
     * Starts processing the payment
     */
    public void startProcessing() {
        if (status != KTypePaymentStatus.PENDING) {
            throw new DomainException(ERR_INVALID_STATUS_TRANSITION,
                    "Can only start processing from PENDING status");
        }
        this.status = KTypePaymentStatus.PROCESSING;
        markAsUpdated();
    }

    /**
     * Marks the payment as completed
     * 
     * @param gatewayTransactionId Transaction ID from payment gateway
     * @param gatewayResponseCode  Response code from gateway
     */
    public void complete(String gatewayTransactionId, String gatewayResponseCode) {
        if (status != KTypePaymentStatus.PROCESSING) {
            throw new DomainException(ERR_INVALID_STATUS_TRANSITION,
                    "Can only complete from PROCESSING status");
        }
        this.status = KTypePaymentStatus.COMPLETED;

        PaymentTransaction transaction = PaymentTransaction.builder()
                .paymentId(this.getId())
                .gatewayTransactionId(gatewayTransactionId)
                .gatewayResponseCode(gatewayResponseCode)
                .status(KTypePaymentStatus.COMPLETED)
                .build();
        transactions.add(transaction);
        markAsUpdated();
    }

    /**
     * Marks the payment as failed
     * 
     * @param gatewayResponseCode Response code from gateway
     * @param rawResponse         Raw response text for debugging
     */
    public void fail(String gatewayResponseCode, String rawResponse) {
        if (status != KTypePaymentStatus.PROCESSING && status != KTypePaymentStatus.PENDING) {
            throw new DomainException(ERR_INVALID_STATUS_TRANSITION,
                    "Can only fail from PENDING or PROCESSING status");
        }
        this.status = KTypePaymentStatus.FAILED;

        PaymentTransaction transaction = PaymentTransaction.builder()
                .paymentId(this.getId())
                .gatewayResponseCode(gatewayResponseCode)
                .rawResponseText(rawResponse)
                .status(KTypePaymentStatus.FAILED)
                .build();
        transactions.add(transaction);
        markAsUpdated();
    }

    /**
     * Initiates a refund for this payment
     * 
     * @param refundAmount Amount to refund
     * @return The created refund transaction
     */
    public RefundTransaction initiateRefund(BigDecimal refundAmount) {
        if (status != KTypePaymentStatus.COMPLETED) {
            throw new DomainException(ERR_INVALID_STATUS_TRANSITION,
                    "Can only refund COMPLETED payments");
        }
        if (status == KTypePaymentStatus.REFUNDED) {
            throw new DomainException(ERR_ALREADY_REFUNDED, "Payment already refunded");
        }
        if (refundAmount.compareTo(this.amount) > 0) {
            throw new DomainException(ERR_INVALID_AMOUNT, "Refund amount exceeds payment amount");
        }

        this.status = KTypePaymentStatus.REFUNDED;
        markAsUpdated();

        return RefundTransaction.builder()
                .paymentTransactionId(getLatestTransactionId())
                .refundAmount(refundAmount)
                .build();
    }

    /**
     * Gets the latest transaction ID
     */
    private String getLatestTransactionId() {
        if (transactions.isEmpty()) {
            return null;
        }
        return transactions.get(transactions.size() - 1).getId();
    }

    /**
     * Gets an unmodifiable list of transactions
     * 
     * @return List of payment transactions
     */
    public List<PaymentTransaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    /**
     * Checks if payment is in a terminal state
     * 
     * @return true if completed, failed, or refunded
     */
    public boolean isTerminal() {
        return status == KTypePaymentStatus.COMPLETED
                || status == KTypePaymentStatus.FAILED
                || status == KTypePaymentStatus.REFUNDED;
    }

    /**
     * Builder class for Payment
     */
    public static class Builder {
        private String id;
        private String orderId;
        private BigDecimal amount;
        private KTypePaymentMethod paymentMethod;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder paymentMethod(KTypePaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        /**
         * Builds the Payment instance
         * 
         * @return New Payment instance
         * @throws NullPointerException if required fields are missing
         */
        public Payment build() {
            return new Payment(this);
        }
    }
}
