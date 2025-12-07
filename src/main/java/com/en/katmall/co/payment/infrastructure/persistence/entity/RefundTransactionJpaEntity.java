/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.infrastructure.persistence.entity;

import com.en.katmall.co.shared.enums.KTypeRefundStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * JPA Entity for Refund Transaction persistence.
 * Maps to 'refund_transactions' table in the database.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Entity
@Table(name = "refund_transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundTransactionJpaEntity {

    @Id
    @Column(length = 255)
    private String id;

    @Column(name = "payment_transaction_id", nullable = false, length = 255)
    private String paymentTransactionId;

    @Column(name = "refund_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal refundAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private KTypeRefundStatus status;

    @Column(name = "gateway_refund_id", length = 255)
    private String gatewayRefundId;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}
