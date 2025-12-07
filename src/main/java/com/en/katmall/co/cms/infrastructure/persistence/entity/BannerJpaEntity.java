/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.infrastructure.persistence.entity;

import com.en.katmall.co.shared.enums.KTypeBannerPosition;
import jakarta.persistence.*;
import lombok.*;

/**
 * JPA Entity for Banner persistence.
 * Maps to 'cms_banners' table in the database.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Entity
@Table(name = "cms_banners")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerJpaEntity {

    @Id
    @Column(length = 255)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "image_url", nullable = false, length = 512)
    private String imageUrl;

    @Column(name = "target_url", length = 512)
    private String targetUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "display_position", length = 50)
    private KTypeBannerPosition displayPosition;

    @Column
    @Builder.Default
    private Integer priority = 0;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}
