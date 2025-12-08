/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.infrastructure.persistence.repository;

import com.en.katmall.co.cms.domain.model.News;
import com.en.katmall.co.cms.domain.repository.NewsRepository;
import com.en.katmall.co.cms.infrastructure.persistence.entity.NewsJpaEntity;
import com.en.katmall.co.cms.infrastructure.persistence.mapper.NewsPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of NewsRepository using Spring Data JPA.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepository {

    private final NewsJpaRepository jpaRepository;
    private final NewsPersistenceMapper mapper;

    @Override
    @Transactional
    public News save(News news) {
        Objects.requireNonNull(news, "News must not be null");
        NewsJpaEntity entity = mapper.toEntity(news);
        NewsJpaEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<News> findById(String id) {
        Objects.requireNonNull(id, "ID must not be null");
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<News> findBySlug(String slug) {
        Objects.requireNonNull(slug, "Slug must not be null");
        return jpaRepository.findBySlug(slug).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<News> findAllPublished() {
        return jpaRepository.findAllPublished().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<News> findByAuthorId(String authorId) {
        Objects.requireNonNull(authorId, "Author ID must not be null");
        return jpaRepository.findByAuthorIdOrderByCreatedAtDesc(authorId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<News> findAll(int page, int size) {
        return jpaRepository.findAllPaged(PageRequest.of(page, size)).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Objects.requireNonNull(id, "ID must not be null");
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(String slug) {
        Objects.requireNonNull(slug, "Slug must not be null");
        return jpaRepository.existsBySlug(slug);
    }
}
