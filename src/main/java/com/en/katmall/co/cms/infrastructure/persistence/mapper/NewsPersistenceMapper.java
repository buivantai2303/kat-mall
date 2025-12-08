/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.infrastructure.persistence.mapper;

import com.en.katmall.co.cms.domain.model.News;
import com.en.katmall.co.cms.infrastructure.persistence.entity.NewsJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Mapper for News domain model and JPA entity conversion.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Component
public class NewsPersistenceMapper {

    /**
     * Converts domain model to JPA entity
     */
    public NewsJpaEntity toEntity(News model) {
        Objects.requireNonNull(model, "News must not be null");

        return NewsJpaEntity.builder()
                .id(model.getId())
                .slug(model.getSlug())
                .authorId(model.getAuthorId())
                .thumbnailUrl(model.getThumbnailUrl())
                .isPublished(model.isPublished())
                .publishedAt(model.getPublishedAt())
                .createdAt(model.getCreatedAt())
                .build();
    }

    /**
     * Converts JPA entity to domain model
     */
    public News toModel(NewsJpaEntity entity) {
        Objects.requireNonNull(entity, "NewsJpaEntity must not be null");

        return News.builder()
                .id(entity.getId())
                .slug(entity.getSlug())
                .authorId(entity.getAuthorId())
                .thumbnailUrl(entity.getThumbnailUrl())
                .isPublished(entity.getIsPublished())
                .build();
    }
}
