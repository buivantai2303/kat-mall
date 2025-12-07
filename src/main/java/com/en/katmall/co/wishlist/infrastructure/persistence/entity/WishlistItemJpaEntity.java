/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.wishlist.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * JPA Entity for Wishlist Item persistence.
 * Maps to 'wishlist_items' table in the database.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Entity
@Table(name = "wishlist_items", indexes = {
        @Index(name = "idx_wishlist_items_product", columnList = "product_id")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = { "wishlist_id", "product_id", "variant_id" })
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemJpaEntity {

    @Id
    @Column(length = 255)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private WishlistJpaEntity wishlist;

    @Column(name = "product_id", length = 255)
    private String productId;

    @Column(name = "variant_id", length = 255)
    private String variantId;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}
