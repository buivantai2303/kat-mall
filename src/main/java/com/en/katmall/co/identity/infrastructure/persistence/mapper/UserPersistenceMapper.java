/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.infrastructure.persistence.mapper;

import com.en.katmall.co.identity.domain.model.UserModel;
import com.en.katmall.co.identity.domain.model.valueobject.Email;
import com.en.katmall.co.identity.infrastructure.persistence.entity.UserJpaEntity;
import com.en.katmall.co.shared.enums.KTypeUserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Mapper for converting between User domain model and UserJpaEntity.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Slf4j
@Component
public class UserPersistenceMapper {

    /**
     * Converts JPA entity to domain model
     * 
     * @param entity JPA entity
     * @return Domain User
     */
    public UserModel toDomain(UserJpaEntity entity) {
        if (entity == null)
            return null;

        // Combine firstName and lastName into fullName for domain
        String fullName = combineNames(entity.getFirstName(), entity.getLastName());

        UserModel userModel = UserModel.create(
                fullName,
                Email.of(entity.getEmail()),
                entity.getUserpassword());

        // Use reflection to restore state
        setField(userModel, "id", entity.getId());
        setField(userModel, "phone", entity.getPhoneNumber());
        setField(userModel, "avatarUrl", entity.getAvatarUrl());
        setField(userModel, "authProvider", entity.getAuthProvider());
        setField(userModel, "providerId", entity.getProviderId());
        setField(userModel, "emailVerified", entity.getIsVerified());
        setField(userModel, "lockedAt", entity.getLockedAt());
        setField(userModel, "lockReason", entity.getLockReason());
        setField(userModel, "createdAt", entity.getCreatedAt());
        setField(userModel, "updatedAt", entity.getUpdatedAt());

        // Map status from boolean flags
        if (Boolean.TRUE.equals(entity.getIsLocked())) {
            setField(userModel, "status", KTypeUserStatus.LOCKED);
        } else if (Boolean.FALSE.equals(entity.getIsActive())) {
            setField(userModel, "status", KTypeUserStatus.INACTIVE);
        } else {
            setField(userModel, "status", KTypeUserStatus.ACTIVE);
        }

        return userModel;
    }

    /**
     * Converts domain model to JPA entity
     * 
     * @param userModel Domain User
     * @return JPA entity
     */
    public UserJpaEntity toEntity(UserModel userModel) {
        if (userModel == null)
            return null;

        // Split fullName into firstName and lastName
        String[] names = splitName(userModel.getFullName());

        return UserJpaEntity.builder()
                .id(userModel.getId())
                .firstName(names[0])
                .lastName(names[1])
                .email(userModel.getEmail().getValue())
                .userpassword(userModel.getPasswordHash())
                .phoneNumber(userModel.getPhone())
                .avatarUrl(userModel.getAvatarUrl())
                .authProvider(userModel.getAuthProvider())
                .providerId(userModel.getProviderId())
                .isActive(userModel.getStatus() == KTypeUserStatus.ACTIVE)
                .isVerified(userModel.isEmailVerified())
                .isLocked(userModel.getStatus() == KTypeUserStatus.LOCKED)
                .lockedAt(userModel.getLockedAt())
                .lockReason(userModel.getLockReason())
                .createdAt(userModel.getCreatedAt())
                .updatedAt(userModel.getUpdatedAt())
                .build();
    }

    /**
     * Updates existing entity from domain model
     * 
     * @param entity Existing entity
     * @param userModel   Updated domain model
     */
    public void updateEntity(UserJpaEntity entity, UserModel userModel) {
        String[] names = splitName(userModel.getFullName());

        entity.setFirstName(names[0]);
        entity.setLastName(names[1]);
        entity.setEmail(userModel.getEmail().getValue());
        entity.setUserpassword(userModel.getPasswordHash());
        entity.setPhoneNumber(userModel.getPhone());
        entity.setAvatarUrl(userModel.getAvatarUrl());
        entity.setAuthProvider(userModel.getAuthProvider());
        entity.setProviderId(userModel.getProviderId());
        entity.setIsActive(userModel.getStatus() == KTypeUserStatus.ACTIVE);
        entity.setIsVerified(userModel.isEmailVerified());
        entity.setIsLocked(userModel.getStatus() == KTypeUserStatus.LOCKED);
        entity.setLockedAt(userModel.getLockedAt());
        entity.setLockReason(userModel.getLockReason());
    }

    /**
     * Splits fullName into firstName and lastName
     */
    private String[] splitName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[] { "", "" };
        }

        String trimmed = fullName.trim();
        int lastSpace = trimmed.lastIndexOf(' ');

        if (lastSpace > 0) {
            return new String[] {
                    trimmed.substring(0, lastSpace).trim(),
                    trimmed.substring(lastSpace + 1).trim()
            };
        }

        // No space found, treat entire name as firstName
        return new String[] { trimmed, "" };
    }

    /**
     * Combines firstName and lastName into fullName
     */
    private String combineNames(String firstName, String lastName) {
        if (firstName == null)
            firstName = "";
        if (lastName == null)
            lastName = "";

        String combined = (firstName + " " + lastName).trim();
        return combined.isEmpty() ? null : combined;
    }

    /**
     * Sets a field value using reflection
     */
    private void setField(Object obj, String fieldName, Object value) {
        try {
            var field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, value);
            }
        } catch (Exception e) {
            // Log and continue
        }
    }

    /**
     * Finds a field using reflection
     */
    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
}
