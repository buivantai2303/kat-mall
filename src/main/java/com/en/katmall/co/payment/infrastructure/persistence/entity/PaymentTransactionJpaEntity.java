/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.payment.infrastructure.persistence.entity;

import com.en.katmall.co.shared.enums.KTypePaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * JPA Entity for Payment Transaction persistence.
 * Maps to 'payment_transactions' table in the database.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Entity
@Table(name = "payment_transactions", indexes = {
        @Index(name = "idx_payment_trans_gateway_id", columnList = "gateway_transaction_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionJpaEntity {

    @Id
    @Column(length = 255)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentJpaEntity payment;

    @Column(name = "gateway_transaction_id", length = 255)
    private String gatewayTransactionId;

    @Column(name = "gateway_response_code", length = 50)
    private String gatewayResponseCode;

    @Column(name = "raw_response_text", columnDefinition = "TEXT")
    private String rawResponseText;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private KTypePaymentStatus status;

    @Column(name = "performed_at")
    private Instant performedAt;
}
