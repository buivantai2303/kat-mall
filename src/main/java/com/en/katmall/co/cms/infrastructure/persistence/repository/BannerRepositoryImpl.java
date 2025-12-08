/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.infrastructure.persistence.repository;

import com.en.katmall.co.cms.domain.model.Banner;
import com.en.katmall.co.cms.domain.repository.BannerRepository;
import com.en.katmall.co.cms.infrastructure.persistence.entity.BannerJpaEntity;
import com.en.katmall.co.cms.infrastructure.persistence.mapper.BannerPersistenceMapper;
import com.en.katmall.co.shared.enums.KTypeBannerPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of BannerRepository using Spring Data JPA.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepository {

    private final BannerJpaRepository jpaRepository;
    private final BannerPersistenceMapper mapper;

    @Override
    @Transactional
    public Banner save(Banner banner) {
        Objects.requireNonNull(banner, "Banner must not be null");
        BannerJpaEntity entity = mapper.toEntity(banner);
        BannerJpaEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Banner> findById(String id) {
        Objects.requireNonNull(id, "ID must not be null");
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Banner> findAllActive() {
        return jpaRepository.findAllActive().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Banner> findActiveByPosition(KTypeBannerPosition position) {
        Objects.requireNonNull(position, "Position must not be null");
        return jpaRepository.findActiveByPosition(position).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Banner> findAll(int page, int size) {
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
}
