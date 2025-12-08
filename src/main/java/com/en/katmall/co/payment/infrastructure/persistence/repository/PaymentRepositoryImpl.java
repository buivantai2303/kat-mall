/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.infrastructure.persistence.repository;

import com.en.katmall.co.payment.domain.model.Payment;
import com.en.katmall.co.payment.domain.repository.PaymentRepository;
import com.en.katmall.co.shared.enums.KTypePaymentStatus;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of PaymentRepository.
 * TODO: Replace with JPA implementation
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final Map<String, Payment> storage = new ConcurrentHashMap<>();

    @Override
    public Payment save(Payment payment) {
        Objects.requireNonNull(payment, "Payment must not be null");
        storage.put(payment.getId(), payment);
        return payment;
    }

    @Override
    public Optional<Payment> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Payment> findByOrderId(String orderId) {
        return storage.values().stream()
                .filter(p -> orderId.equals(p.getOrderId()))
                .findFirst();
    }

    @Override
    public List<Payment> findAllByOrderId(String orderId) {
        return storage.values().stream()
                .filter(p -> orderId.equals(p.getOrderId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByStatus(KTypePaymentStatus status) {
        return storage.values().stream()
                .filter(p -> status.equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findPendingPayments() {
        return storage.values().stream()
                .filter(p -> KTypePaymentStatus.PENDING.equals(p.getStatus()))
                .collect(Collectors.toList());
    }
}
