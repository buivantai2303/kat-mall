/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.infrastructure.persistence.mapper;

import com.en.katmall.co.cms.domain.model.Banner;
import com.en.katmall.co.cms.infrastructure.persistence.entity.BannerJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Mapper for Banner domain model and JPA entity conversion.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Component
public class BannerPersistenceMapper {

    /**
     * Converts domain model to JPA entity
     */
    public BannerJpaEntity toEntity(Banner model) {
        Objects.requireNonNull(model, "Banner must not be null");

        return BannerJpaEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .imageUrl(model.getImageUrl())
                .targetUrl(model.getTargetUrl())
                .displayPosition(model.getDisplayPosition())
                .priority(model.getPriority())
                .isActive(model.isActive())
                .build();
    }

    /**
     * Converts JPA entity to domain model
     */
    public Banner toModel(BannerJpaEntity entity) {
        Objects.requireNonNull(entity, "BannerJpaEntity must not be null");

        return Banner.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .targetUrl(entity.getTargetUrl())
                .displayPosition(entity.getDisplayPosition())
                .priority(entity.getPriority())
                .isActive(entity.getIsActive())
                .build();
    }
}
