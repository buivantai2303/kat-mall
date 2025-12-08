/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.infrastructure.persistence.repository;

import com.en.katmall.co.identity.domain.model.UserModel;
import com.en.katmall.co.identity.domain.model.valueobject.Email;
import com.en.katmall.co.identity.domain.repository.UserRepository;
import com.en.katmall.co.identity.infrastructure.persistence.entity.UserJpaEntity;
import com.en.katmall.co.identity.infrastructure.persistence.mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementation of UserRepository using JPA.
 * Bridges domain repository interface with Spring Data JPA.
 * 
 * <p>
 * This class acts as an adapter between the domain layer's
 * {@link UserRepository}
 * interface and the infrastructure layer's {@link UserJpaRepositorySpring}.
 * It uses {@link UserPersistenceMapper} to convert between domain models and
 * JPA entities.
 * 
 * @author tai.buivan
 * @version 1.0
 * @see UserRepository
 * @see UserJpaRepositorySpring
 * @see UserPersistenceMapper
 */
@Repository
@RequiredArgsConstructor
public class UserJpaRepository implements UserRepository {

    /** Spring Data JPA repository for database operations */
    private final UserJpaRepositorySpring jpaRepository;

    /** Mapper for converting between domain and entity objects */
    private final UserPersistenceMapper mapper;

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Converts the domain User to JPA entity, persists it,
     * and returns the saved entity converted back to domain model.
     */
    @Override
    public UserModel save(UserModel userModel) {
        UserJpaEntity entity = mapper.toEntity(userModel);
        UserJpaEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Queries the database by primary key and maps result to domain model.
     */
    @Override
    public Optional<UserModel> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toModel);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Queries the database by email value (case-insensitive)
     * and maps result to domain model.
     */
    @Override
    public Optional<UserModel> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
                .map(mapper::toModel);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Checks existence by email value using Spring Data's derived query.
     */
    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Finds only active users, excluding deactivated accounts.
     * Used for login flow.
     */
    @Override
    public Optional<UserModel> findActiveByEmail(Email email) {
        return jpaRepository.findActiveByEmail(email.getValue())
                .map(mapper::toModel);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Checks if an active user with this email exists.
     * Allows re-registration of deactivated accounts.
     */
    @Override
    public boolean existsActiveByEmail(Email email) {
        return jpaRepository.existsActiveByEmail(email.getValue());
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Checks existence by phone number using Spring Data's derived query.
     */
    @Override
    public boolean existsByPhone(String phone) {
        return jpaRepository.existsByPhoneNumber(phone);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * <strong>Note:</strong> This performs a HARD DELETE.
     * For soft delete functionality, use {@link UserModel#deactivate()} instead.
     */
    @Override
    public void delete(UserModel userModel) {
        jpaRepository.deleteById(userModel.getId());
    }
}
