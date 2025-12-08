/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.infrastructure.persistence.repository;

import com.en.katmall.co.payment.domain.model.RefundTransaction;
import com.en.katmall.co.payment.domain.repository.RefundRepository;
import com.en.katmall.co.shared.enums.KTypeRefundStatus;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of RefundRepository.
 * TODO: Replace with JPA implementation
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public class RefundRepositoryImpl implements RefundRepository {

    private final Map<String, RefundTransaction> storage = new ConcurrentHashMap<>();

    @Override
    public RefundTransaction save(RefundTransaction refund) {
        Objects.requireNonNull(refund, "RefundTransaction must not be null");
        storage.put(refund.getId(), refund);
        return refund;
    }

    @Override
    public Optional<RefundTransaction> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<RefundTransaction> findByPaymentTransactionId(String paymentTransactionId) {
        return storage.values().stream()
                .filter(r -> paymentTransactionId.equals(r.getPaymentTransactionId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundTransaction> findByStatus(KTypeRefundStatus status) {
        return storage.values().stream()
                .filter(r -> status.equals(r.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundTransaction> findPendingRefunds() {
        return storage.values().stream()
                .filter(r -> KTypeRefundStatus.PENDING.equals(r.getStatus()))
                .collect(Collectors.toList());
    }
}
