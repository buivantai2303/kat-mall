/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.infrastructure.persistence.repository;

import com.en.katmall.co.identity.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for UserJpaEntity.
 * 
 * <p>
 * Provides both standard queries (all users) and active-only queries
 * to support soft delete functionality where inactive users can re-register.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public interface UserJpaRepositorySpring extends JpaRepository<UserJpaEntity, String> {

    /**
     * Finds user by email (includes inactive users)
     * 
     * @param email User email
     * @return Optional containing user if found
     */
    Optional<UserJpaEntity> findByEmail(String email);

    /**
     * Finds active user by email (excludes inactive/deleted users)
     * 
     * @param email User email
     * @return Optional containing active user if found
     */
    @Query("SELECT u FROM UserJpaEntity u WHERE u.email = :email AND u.isActive = true")
    Optional<UserJpaEntity> findActiveByEmail(@Param("email") String email);

    /**
     * Finds user by phone number
     * 
     * @param phoneNumber Phone number
     * @return Optional containing user if found
     */
    Optional<UserJpaEntity> findByPhoneNumber(String phoneNumber);

    /**
     * Checks if email exists (all users including inactive)
     * 
     * @param email Email to check
     * @return true if exists
     */
    boolean existsByEmail(String email);

    /**
     * Checks if email exists for active users only
     * Used for registration to allow re-registration of deactivated accounts
     * 
     * @param email Email to check
     * @return true if active user with this email exists
     */
    @Query("SELECT COUNT(u) > 0 FROM UserJpaEntity u WHERE u.email = :email AND u.isActive = true")
    boolean existsActiveByEmail(@Param("email") String email);

    /**
     * Checks if phone exists
     * 
     * @param phoneNumber Phone to check
     * @return true if exists
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Finds user by social provider info
     * 
     * @param provider   Auth provider
     * @param providerId Provider user ID
     * @return Optional containing user if found
     */
    @Query("SELECT u FROM UserJpaEntity u WHERE u.authProvider = :provider AND u.providerId = :providerId")
    Optional<UserJpaEntity> findByProviderAndProviderId(
            @Param("provider") com.en.katmall.co.shared.enums.KTypeAuthProvider provider,
            @Param("providerId") String providerId);

    /**
     * Finds active user by social provider info
     * 
     * @param provider   Auth provider
     * @param providerId Provider user ID
     * @return Optional containing active user if found
     */
    @Query("SELECT u FROM UserJpaEntity u WHERE u.authProvider = :provider AND u.providerId = :providerId AND u.isActive = true")
    Optional<UserJpaEntity> findActiveByProviderAndProviderId(
            @Param("provider") com.en.katmall.co.shared.enums.KTypeAuthProvider provider,
            @Param("providerId") String providerId);
}
