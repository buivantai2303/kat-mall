/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.application.usecase;

import com.en.katmall.co.payment.domain.model.Payment;
import com.en.katmall.co.payment.domain.repository.PaymentRepository;
import com.en.katmall.co.shared.enums.KTypePaymentMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Use Case: Create Payment
 * Creates a new payment for an order.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreatePaymentUseCase {

    private final PaymentRepository paymentRepository;

    /**
     * Executes the create payment use case.
     * 
     * @param orderId       The order ID
     * @param amount        Payment amount
     * @param paymentMethod Payment method
     * @return Created payment
     */
    public Payment execute(String orderId, BigDecimal amount, KTypePaymentMethod paymentMethod) {
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(paymentMethod, "paymentMethod must not be null");

        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .build();

        log.info("Created payment {} for order {} with amount {}", payment.getId(), orderId, amount);
        return paymentRepository.save(payment);
    }
}
