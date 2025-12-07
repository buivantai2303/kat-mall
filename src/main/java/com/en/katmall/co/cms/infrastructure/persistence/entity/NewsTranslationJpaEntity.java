/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * JPA Entity for News Translation persistence.
 * Maps to 'cms_news_translations' table in the database.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Entity
@Table(name = "cms_news_translations", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "news_id", "language_code" })
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsTranslationJpaEntity {

    @Id
    @Column(length = 255)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private NewsJpaEntity news;

    @Column(name = "language_code", nullable = false, length = 5)
    private String languageCode;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;
}
