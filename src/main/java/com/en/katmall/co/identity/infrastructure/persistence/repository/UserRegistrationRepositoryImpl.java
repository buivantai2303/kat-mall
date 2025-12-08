/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.infrastructure.persistence.repository;

import com.en.katmall.co.identity.domain.model.UserRegistrationModel;
import com.en.katmall.co.identity.domain.repository.UserRegistrationRepository;
import com.en.katmall.co.identity.infrastructure.persistence.entity.UserRegistrationJpaEntity;
import com.en.katmall.co.identity.infrastructure.persistence.mapper.UserRegistrationPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA implementation of UserRegistrationRepository.
 * Stores pending registrations in the user_registrations table.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class UserRegistrationRepositoryImpl implements UserRegistrationRepository {

    private final UserRegistrationJpaRepository jpaRepository;
    private final UserRegistrationPersistenceMapper mapper;

    @Override
    @Transactional
    public UserRegistrationModel save(UserRegistrationModel registration) {
        Objects.requireNonNull(registration, "UserRegistrationModel must not be null");
        UserRegistrationJpaEntity entity = mapper.toEntity(registration);
        UserRegistrationJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<UserRegistrationModel> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<UserRegistrationModel> findByIdentifier(String identifier) {
        return jpaRepository.findByIdentifier(identifier)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<UserRegistrationModel> findByVerificationToken(String token) {
        return jpaRepository.findByVerificationToken(token)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByIdentifier(String identifier) {
        return jpaRepository.existsByIdentifier(identifier);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByIdentifier(String identifier) {
        jpaRepository.deleteByIdentifier(identifier);
    }

    @Override
    public List<UserRegistrationModel> findExpiredBefore(Instant before) {
        return jpaRepository.findExpiredBefore(before).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int deleteExpiredBefore(Instant before) {
        return jpaRepository.deleteExpiredBefore(before);
    }
}
