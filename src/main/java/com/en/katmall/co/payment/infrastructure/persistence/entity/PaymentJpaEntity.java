/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.infrastructure.persistence.entity;

import com.en.katmall.co.shared.enums.KTypePaymentMethod;
import com.en.katmall.co.shared.enums.KTypePaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity for Payment persistence.
 * Maps to 'payments' table in the database.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payments_order_id", columnList = "order_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentJpaEntity {

    @Id
    @Column(length = 255)
    private String id;

    @Column(name = "order_id", nullable = false, length = 255)
    private String orderId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 50)
    private KTypePaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private KTypePaymentStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PaymentTransactionJpaEntity> transactions = new ArrayList<>();

    /**
     * Adds a transaction to this payment
     * 
     * @param transaction The transaction to add
     */
    public void addTransaction(PaymentTransactionJpaEntity transaction) {
        transactions.add(transaction);
        transaction.setPayment(this);
    }
}
