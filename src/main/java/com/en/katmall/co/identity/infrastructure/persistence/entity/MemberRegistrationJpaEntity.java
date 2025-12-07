/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.infrastructure.persistence.entity;

import com.en.katmall.co.shared.enums.KTypeIdentifier;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * JPA Entity for Member Registration persistence.
 * Maps to 'member_registrations' table in the database.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Entity
@Table(name = "member_registrations", indexes = {
        @Index(name = "idx_member_reg_identifier", columnList = "identifier"),
        @Index(name = "idx_member_reg_token", columnList = "verification_token"),
        @Index(name = "idx_member_reg_expires", columnList = "expires_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegistrationJpaEntity {

    @Id
    @Column(length = 255)
    private String id;

    @Column(nullable = false, unique = true, length = 255)
    private String identifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "identifier_type", nullable = false, length = 20)
    private KTypeIdentifier identifierType;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "verification_token", nullable = false, unique = true, length = 255)
    private String verificationToken;

    @Column(name = "verification_attempts")
    @Builder.Default
    private Integer verificationAttempts = 1;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "last_sent_at")
    private Instant lastSentAt;

    @Column(name = "is_verified")
    @Builder.Default
    private Boolean verified = false;
}
