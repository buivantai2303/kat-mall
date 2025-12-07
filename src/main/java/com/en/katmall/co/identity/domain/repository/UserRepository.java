/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.domain.repository;

import com.en.katmall.co.identity.domain.model.UserModel;
import com.en.katmall.co.identity.domain.model.valueobject.Email;

import java.util.Optional;

/**
 * Repository interface for User aggregate.
 * Defines persistence operations for User domain model.
 * 
 * <p>
 * Supports soft delete functionality through active-only queries,
 * allowing users to re-register after account deactivation.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface UserRepository {

    /**
     * Saves a user (create or update)
     * 
     * @param userModel User to save
     * @return Saved user
     */
    UserModel save(UserModel userModel);

    /**
     * Finds user by ID
     * 
     * @param id User ID
     * @return Optional containing user if found
     */
    Optional<UserModel> findById(String id);

    /**
     * Finds user by email (includes inactive users)
     * 
     * @param email User email
     * @return Optional containing user if found
     */
    Optional<UserModel> findByEmail(Email email);

    /**
     * Finds active user by email (excludes deactivated users)
     * Used for login flow to only allow active users
     * 
     * @param email User email
     * @return Optional containing active user if found
     */
    Optional<UserModel> findActiveByEmail(Email email);

    /**
     * Checks if email exists (all users including inactive)
     * 
     * @param email Email to check
     * @return true if exists
     */
    boolean existsByEmail(Email email);

    /**
     * Checks if email exists for active users only
     * Used for registration to allow re-registration of deactivated accounts
     * 
     * @param email Email to check
     * @return true if active user with this email exists
     */
    boolean existsActiveByEmail(Email email);

    /**
     * Checks if phone exists
     * 
     * @param phone Phone to check
     * @return true if exists
     */
    boolean existsByPhone(String phone);

    /**
     * Deletes a user (hard delete)
     * Consider using User.deactivate() for soft delete
     * 
     * @param userModel User to delete
     */
    void delete(UserModel userModel);
}
