/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.wishlist.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity for Wishlist persistence.
 * Maps to 'wishlists' table in the database.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Entity
@Table(name = "wishlists", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistJpaEntity {

    @Id
    @Column(length = 255)
    private String id;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<WishlistItemJpaEntity> items = new ArrayList<>();

    /**
     * Adds an item to this wishlist
     * 
     * @param item The item to add
     */
    public void addItem(WishlistItemJpaEntity item) {
        items.add(item);
        item.setWishlist(this);
    }

    /**
     * Removes an item from this wishlist
     * 
     * @param item The item to remove
     */
    public void removeItem(WishlistItemJpaEntity item) {
        items.remove(item);
        item.setWishlist(null);
    }
}
