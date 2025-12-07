/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.domain.repository;

import com.en.katmall.co.payment.domain.model.RefundTransaction;
import com.en.katmall.co.shared.enums.KTypeRefundStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RefundTransaction entity.
 * Defines the contract for refund persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface RefundRepository {

    /**
     * Saves a refund transaction
     * 
     * @param refund The refund to save
     * @return The saved refund
     */
    RefundTransaction save(RefundTransaction refund);

    /**
     * Finds refund by ID
     * 
     * @param id The refund ID
     * @return Optional containing the refund if found
     */
    Optional<RefundTransaction> findById(String id);

    /**
     * Finds refunds by payment transaction ID
     * 
     * @param paymentTransactionId The payment transaction ID
     * @return List of refunds for the transaction
     */
    List<RefundTransaction> findByPaymentTransactionId(String paymentTransactionId);

    /**
     * Finds refunds by status
     * 
     * @param status The refund status
     * @return List of refunds with the specified status
     */
    List<RefundTransaction> findByStatus(KTypeRefundStatus status);

    /**
     * Finds pending refunds that need processing
     * 
     * @return List of pending refunds
     */
    List<RefundTransaction> findPendingRefunds();
}
