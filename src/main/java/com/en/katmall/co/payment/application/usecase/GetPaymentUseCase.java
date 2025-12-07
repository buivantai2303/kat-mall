/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.application.usecase;

import com.en.katmall.co.payment.domain.model.Payment;
import com.en.katmall.co.payment.domain.repository.PaymentRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Get Payment
 * Retrieves payment information.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPaymentUseCase {

    private final PaymentRepository paymentRepository;

    /**
     * Finds a payment by ID.
     * 
     * @param id The payment ID
     * @return The payment
     * @throws ResourceNotFoundException if not found
     */
    public Payment executeById(String id) {
        Objects.requireNonNull(id, "Payment ID must not be null");
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", id));
    }

    /**
     * Finds payment for an order.
     * 
     * @param orderId The order ID
     * @return The payment
     * @throws ResourceNotFoundException if not found
     */
    public Payment executeByOrderId(String orderId) {
        Objects.requireNonNull(orderId, "Order ID must not be null");
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "orderId", orderId));
    }
}
