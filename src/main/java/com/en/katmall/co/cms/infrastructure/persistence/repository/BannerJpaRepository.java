/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.infrastructure.persistence.repository;

import com.en.katmall.co.cms.infrastructure.persistence.entity.BannerJpaEntity;
import com.en.katmall.co.shared.enums.KTypeBannerPosition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for BannerJpaEntity.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public interface BannerJpaRepository extends JpaRepository<BannerJpaEntity, String> {

    /**
     * Finds all active banners ordered by priority
     */
    @Query("SELECT b FROM BannerJpaEntity b WHERE b.isActive = true ORDER BY b.priority DESC")
    List<BannerJpaEntity> findAllActive();

    /**
     * Finds active banners by display position
     */
    @Query("SELECT b FROM BannerJpaEntity b WHERE b.isActive = true AND b.displayPosition = :position ORDER BY b.priority DESC")
    List<BannerJpaEntity> findActiveByPosition(@Param("position") KTypeBannerPosition position);

    /**
     * Finds all banners with pagination
     */
    @Query("SELECT b FROM BannerJpaEntity b ORDER BY b.priority DESC")
    List<BannerJpaEntity> findAllPaged(Pageable pageable);
}
