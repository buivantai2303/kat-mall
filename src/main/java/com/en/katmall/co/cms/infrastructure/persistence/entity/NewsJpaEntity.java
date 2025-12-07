/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity for News persistence.
 * Maps to 'cms_news' table in the database.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Entity
@Table(name = "cms_news", indexes = {
        @Index(name = "idx_cms_news_slug", columnList = "slug"),
        @Index(name = "idx_cms_news_author", columnList = "author_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsJpaEntity {

    @Id
    @Column(length = 255)
    private String id;

    @Column(nullable = false, unique = true, length = 255)
    private String slug;

    @Column(name = "thumbnail_url", length = 512)
    private String thumbnailUrl;

    @Column(name = "author_id", length = 255)
    private String authorId;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "is_published")
    @Builder.Default
    private Boolean isPublished = false;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<NewsTranslationJpaEntity> translations = new ArrayList<>();

    /**
     * Adds a translation to this news article
     * 
     * @param translation The translation to add
     */
    public void addTranslation(NewsTranslationJpaEntity translation) {
        translations.add(translation);
        translation.setNews(this);
    }

    /**
     * Removes a translation from this news article
     * 
     * @param translation The translation to remove
     */
    public void removeTranslation(NewsTranslationJpaEntity translation) {
        translations.remove(translation);
        translation.setNews(null);
    }
}
