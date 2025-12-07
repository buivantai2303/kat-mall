/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.domain.repository;

import com.en.katmall.co.identity.domain.model.MemberRegistrationModel;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MemberRegistrationModel entity.
 * Defines the contract for member registration persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface MemberRegistrationRepository {

    /**
     * Saves a member registration
     * 
     * @param registration The registration to save
     * @return The saved registration
     */
    MemberRegistrationModel save(MemberRegistrationModel registration);

    /**
     * Finds registration by ID
     * 
     * @param id The registration ID
     * @return Optional containing the registration if found
     */
    Optional<MemberRegistrationModel> findById(String id);

    /**
     * Finds registration by identifier (email or phone)
     * 
     * @param identifier The email or phone
     * @return Optional containing the registration if found
     */
    Optional<MemberRegistrationModel> findByIdentifier(String identifier);

    /**
     * Finds registration by verification token
     * 
     * @param token The verification token
     * @return Optional containing the registration if found
     */
    Optional<MemberRegistrationModel> findByVerificationToken(String token);

    /**
     * Checks if identifier already has pending registration
     * 
     * @param identifier The email or phone
     * @return true if registration exists
     */
    boolean existsByIdentifier(String identifier);

    /**
     * Deletes a registration by ID
     * 
     * @param id The registration ID
     */
    void deleteById(String id);

    /**
     * Deletes a registration by identifier
     * 
     * @param identifier The email or phone
     */
    void deleteByIdentifier(String identifier);

    /**
     * Finds all expired registrations for cleanup
     * 
     * @param before Delete registrations created before this time
     * @return List of expired registrations
     */
    List<MemberRegistrationModel> findExpiredBefore(Instant before);

    /**
     * Deletes all expired registrations
     * 
     * @param before Delete registrations created before this time
     * @return Number of deleted records
     */
    int deleteExpiredBefore(Instant before);
}
