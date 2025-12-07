/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.domain.repository;

import com.en.katmall.co.payment.domain.model.Payment;
import com.en.katmall.co.shared.enums.KTypePaymentStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Payment aggregate root.
 * Defines the contract for payment persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface PaymentRepository {

    /**
     * Saves a payment
     * 
     * @param payment The payment to save
     * @return The saved payment
     */
    Payment save(Payment payment);

    /**
     * Finds payment by ID
     * 
     * @param id The payment ID
     * @return Optional containing the payment if found
     */
    Optional<Payment> findById(String id);

    /**
     * Finds payment by order ID
     * 
     * @param orderId The order ID
     * @return Optional containing the payment if found
     */
    Optional<Payment> findByOrderId(String orderId);

    /**
     * Finds all payments for an order
     * 
     * @param orderId The order ID
     * @return List of payments for the order
     */
    List<Payment> findAllByOrderId(String orderId);

    /**
     * Finds payments by status
     * 
     * @param status The payment status
     * @return List of payments with the specified status
     */
    List<Payment> findByStatus(KTypePaymentStatus status);

    /**
     * Finds pending payments that need processing
     * 
     * @return List of pending payments
     */
    List<Payment> findPendingPayments();
}
