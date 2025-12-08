/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.infrastructure.persistence.repository;

import com.en.katmall.co.identity.infrastructure.persistence.entity.UserRegistrationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for UserRegistration.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface UserRegistrationJpaRepository extends JpaRepository<UserRegistrationJpaEntity, String> {

    /**
     * Finds registration by identifier (email or phone)
     */
    Optional<UserRegistrationJpaEntity> findByIdentifier(String identifier);

    /**
     * Finds registration by verification token
     */
    Optional<UserRegistrationJpaEntity> findByVerificationToken(String token);

    /**
     * Checks if identifier exists
     */
    boolean existsByIdentifier(String identifier);

    /**
     * Deletes by identifier
     */
    void deleteByIdentifier(String identifier);

    /**
     * Finds all expired registrations
     */
    @Query("SELECT r FROM UserRegistrationJpaEntity r WHERE r.expiresAt < :before")
    List<UserRegistrationJpaEntity> findExpiredBefore(@Param("before") Instant before);

    /**
     * Deletes all expired registrations
     */
    @Modifying
    @Query("DELETE FROM UserRegistrationJpaEntity r WHERE r.expiresAt < :before")
    int deleteExpiredBefore(@Param("before") Instant before);
}
