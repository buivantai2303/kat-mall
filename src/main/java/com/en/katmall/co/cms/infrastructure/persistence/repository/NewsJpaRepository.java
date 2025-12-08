/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.infrastructure.persistence.repository;

import com.en.katmall.co.cms.infrastructure.persistence.entity.NewsJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for NewsJpaEntity.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public interface NewsJpaRepository extends JpaRepository<NewsJpaEntity, String> {

    Optional<NewsJpaEntity> findBySlug(String slug);

    @Query("SELECT n FROM NewsJpaEntity n WHERE n.isPublished = true ORDER BY n.publishedAt DESC")
    List<NewsJpaEntity> findAllPublished();

    List<NewsJpaEntity> findByAuthorIdOrderByCreatedAtDesc(String authorId);

    @Query("SELECT n FROM NewsJpaEntity n ORDER BY n.createdAt DESC")
    List<NewsJpaEntity> findAllPaged(Pageable pageable);

    boolean existsBySlug(String slug);
}
