/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.domain.model;

import com.en.katmall.co.identity.domain.model.valueobject.Email;
import com.en.katmall.co.identity.domain.event.UserRegisteredEvent;
import com.en.katmall.co.identity.domain.event.UserLockedEvent;
import com.en.katmall.co.shared.domain.AggregateRoot;
import com.en.katmall.co.shared.enums.KTypeAuthProvider;
import com.en.katmall.co.shared.enums.KTypeUserRole;
import com.en.katmall.co.shared.enums.KTypeUserStatus;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

/**
 * User aggregate root representing a customer in the system.
 * Handles user authentication, profile management, and account status.
 * Supports both local and OAuth authentication providers.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class UserModel extends AggregateRoot<String> {

    private String fullName;
    private Email email;
    private String passwordHash;
    private String phone;
    private String avatarUrl;
    private KTypeAuthProvider authProvider;
    private String providerId;
    private KTypeUserStatus status;
    private KTypeUserRole role;
    private boolean emailVerified;
    private Instant lastLoginAt;
    private Instant lockedAt;
    private String lockReason;

    /** Default constructor for JPA */
    protected UserModel() {
        super();
    }

    /**
     * Private constructor for internal use
     */
    private UserModel(String id, String fullName, Email email, String passwordHash,
                      KTypeAuthProvider authProvider, String providerId) {
        super(id);
        this.fullName = Objects.requireNonNull(fullName, "fullName must not be null");
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.passwordHash = passwordHash;
        this.authProvider = Objects.requireNonNull(authProvider, "authProvider must not be null");
        this.providerId = providerId;
        this.status = KTypeUserStatus.ACTIVE;
        this.role = KTypeUserRole.USER;
        this.emailVerified = false;
    }

    /**
     * Creates a new user with email/password registration
     * 
     * @param fullName     User's full name
     * @param email        User's email
     * @param passwordHash Encoded password
     * @return New User instance
     * @throws NullPointerException if required parameters are null
     */
    public static UserModel create(String fullName, Email email, String passwordHash) {
        Objects.requireNonNull(fullName, "fullName must not be null");
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(passwordHash, "passwordHash must not be null for local auth");

        String id = IdGenerator.generate();
        UserModel userModel = new UserModel(id, fullName, email, passwordHash, KTypeAuthProvider.LOCAL, null);
        userModel.registerEvent(new UserRegisteredEvent(id, email.getValue()));
        return userModel;
    }

    /**
     * Creates a new user from social provider
     * 
     * @param fullName   User's name from provider
     * @param email      User's email from provider
     * @param provider   Social auth provider
     * @param providerId Provider user ID
     * @return New User instance
     * @throws NullPointerException if required parameters are null
     */
    public static UserModel createFromSocialProvider(String fullName, Email email,
                                                     KTypeAuthProvider provider, String providerId) {
        Objects.requireNonNull(fullName, "fullName must not be null");
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(provider, "provider must not be null");
        Objects.requireNonNull(providerId, "providerId must not be null for social auth");

        String id = IdGenerator.generate();
        UserModel userModel = new UserModel(id, fullName, email, null, provider, providerId);
        userModel.emailVerified = true; // Social provider emails are pre-verified
        userModel.registerEvent(new UserRegisteredEvent(id, email.getValue()));
        return userModel;
    }

    /**
     * Records user login timestamp
     */
    public void recordLogin() {
        this.lastLoginAt = Instant.now();
        markAsUpdated();
    }

    /**
     * Links a social provider to existing account
     * 
     * @param provider   The social provider
     * @param providerId Provider user ID
     */
    public void linkSocialProvider(KTypeAuthProvider provider, String providerId) {
        Objects.requireNonNull(provider, "provider must not be null");
        Objects.requireNonNull(providerId, "providerId must not be null");

        if (this.authProvider == KTypeAuthProvider.LOCAL) {
            this.authProvider = provider;
            this.providerId = providerId;
            markAsUpdated();
        }
    }

    /**
     * Locks the user account
     * 
     * @param reason Lock reason
     */
    public void lock(String reason) {
        Objects.requireNonNull(reason, "lock reason must not be null");

        if (this.status == KTypeUserStatus.LOCKED)
            return;

        this.status = KTypeUserStatus.LOCKED;
        this.lockedAt = Instant.now();
        this.lockReason = reason;
        markAsUpdated();
        registerEvent(new UserLockedEvent(this.id, reason));
    }

    /**
     * Unlocks the user account
     */
    public void unlock() {
        this.status = KTypeUserStatus.ACTIVE;
        this.lockedAt = null;
        this.lockReason = null;
        markAsUpdated();
    }

    /**
     * Verifies user's email
     */
    public void verifyEmail() {
        this.emailVerified = true;
        markAsUpdated();
    }

    /**
     * Deactivates user account (soft delete)
     */
    public void deactivate() {
        this.status = KTypeUserStatus.INACTIVE;
        markAsUpdated();
    }

    /**
     * Activates user account (reactivate after soft delete)
     */
    public void activate() {
        this.status = KTypeUserStatus.ACTIVE;
        markAsUpdated();
    }

    /**
     * Updates full name
     * 
     * @param fullName New full name
     */
    public void updateFullName(String fullName) {
        this.fullName = Objects.requireNonNull(fullName, "fullName must not be null");
        markAsUpdated();
    }

    /**
     * Updates phone number
     * 
     * @param phone New phone number
     */
    public void updatePhone(String phone) {
        this.phone = phone;
        markAsUpdated();
    }

    /**
     * Updates avatar URL
     * 
     * @param avatarUrl New avatar URL
     */
    public void updateAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        markAsUpdated();
    }

    /**
     * Changes password
     * 
     * @param newPasswordHash New encoded password
     */
    public void changePassword(String newPasswordHash) {
        this.passwordHash = Objects.requireNonNull(newPasswordHash, "password must not be null");
        markAsUpdated();
    }

    /**
     * Promotes user to seller role
     */
    public void promoteToSeller() {
        this.role = KTypeUserRole.SELLER;
        markAsUpdated();
    }

    /**
     * Promotes user to admin role
     */
    public void promoteToAdmin() {
        this.role = KTypeUserRole.ADMIN;
        markAsUpdated();
    }

    /**
     * Checks if user is active
     * 
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return status == KTypeUserStatus.ACTIVE;
    }

    /**
     * Checks if user is locked
     * 
     * @return true if status is LOCKED
     */
    public boolean isLocked() {
        return status == KTypeUserStatus.LOCKED;
    }
}
