/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.application.usecase;

import com.en.katmall.co.payment.domain.model.Payment;
import com.en.katmall.co.payment.domain.repository.PaymentRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Process Payment
 * Handles payment processing lifecycle (start, complete, fail).
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;

    /**
     * Starts processing a payment.
     * 
     * @param paymentId The payment ID
     * @return Updated payment
     * @throws ResourceNotFoundException if not found
     */
    public Payment startProcessing(String paymentId) {
        Objects.requireNonNull(paymentId, "Payment ID must not be null");
        Payment payment = findById(paymentId);
        payment.startProcessing();
        log.info("Started processing payment {}", paymentId);
        return paymentRepository.save(payment);
    }

    /**
     * Completes a payment successfully.
     * 
     * @param paymentId            The payment ID
     * @param gatewayTransactionId Transaction ID from gateway
     * @param gatewayResponseCode  Response code from gateway
     * @return Completed payment
     * @throws ResourceNotFoundException if not found
     */
    public Payment complete(String paymentId, String gatewayTransactionId, String gatewayResponseCode) {
        Objects.requireNonNull(paymentId, "Payment ID must not be null");
        Payment payment = findById(paymentId);
        payment.complete(gatewayTransactionId, gatewayResponseCode);
        log.info("Completed payment {} with gateway transaction {}", paymentId, gatewayTransactionId);
        return paymentRepository.save(payment);
    }

    /**
     * Marks a payment as failed.
     * 
     * @param paymentId           The payment ID
     * @param gatewayResponseCode Response code from gateway
     * @param rawResponse         Raw response for debugging
     * @return Failed payment
     * @throws ResourceNotFoundException if not found
     */
    public Payment fail(String paymentId, String gatewayResponseCode, String rawResponse) {
        Objects.requireNonNull(paymentId, "Payment ID must not be null");
        Payment payment = findById(paymentId);
        payment.fail(gatewayResponseCode, rawResponse);
        log.warn("Payment {} failed with code {}", paymentId, gatewayResponseCode);
        return paymentRepository.save(payment);
    }

    /**
     * Finds a payment by ID.
     * 
     * @param id The payment ID
     * @return The payment
     * @throws ResourceNotFoundException if not found
     */
    private Payment findById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", id));
    }
}
