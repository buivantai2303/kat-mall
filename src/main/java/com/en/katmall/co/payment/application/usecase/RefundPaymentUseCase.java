/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.application.usecase;

import com.en.katmall.co.payment.domain.model.Payment;
import com.en.katmall.co.payment.domain.model.RefundTransaction;
import com.en.katmall.co.payment.domain.repository.PaymentRepository;
import com.en.katmall.co.payment.domain.repository.RefundRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Use Case: Refund Payment
 * Handles refund initialization and completion.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefundPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;

    /**
     * Initiates a refund for a payment.
     * 
     * @param paymentId    The payment ID
     * @param refundAmount Amount to refund
     * @return Created refund transaction
     * @throws ResourceNotFoundException if payment not found
     */
    public RefundTransaction initiateRefund(String paymentId, BigDecimal refundAmount) {
        Objects.requireNonNull(paymentId, "Payment ID must not be null");
        Objects.requireNonNull(refundAmount, "Refund amount must not be null");

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

        RefundTransaction refund = payment.initiateRefund(refundAmount);

        paymentRepository.save(payment);
        RefundTransaction savedRefund = refundRepository.save(refund);

        log.info("Initiated refund {} for payment {} with amount {}",
                savedRefund.getId(), paymentId, refundAmount);
        return savedRefund;
    }

    /**
     * Completes a refund.
     * 
     * @param refundId        The refund ID
     * @param gatewayRefundId Refund ID from gateway
     * @return Completed refund
     * @throws ResourceNotFoundException if refund not found
     */
    public RefundTransaction completeRefund(String refundId, String gatewayRefundId) {
        Objects.requireNonNull(refundId, "Refund ID must not be null");

        RefundTransaction refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund", refundId));

        refund.startProcessing();
        refund.complete(gatewayRefundId);

        log.info("Completed refund {} with gateway ID {}", refundId, gatewayRefundId);
        return refundRepository.save(refund);
    }
}
