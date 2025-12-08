/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.infrastructure.persistence.mapper;

import com.en.katmall.co.identity.domain.model.UserRegistrationModel;
import com.en.katmall.co.identity.infrastructure.persistence.entity.UserRegistrationJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for UserRegistration persistence.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Component
public class UserRegistrationPersistenceMapper {

    /**
     * Converts domain model to JPA entity.
     */
    public UserRegistrationJpaEntity toEntity(UserRegistrationModel model) {
        if (model == null) {
            return null;
        }

        return UserRegistrationJpaEntity.builder()
                .id(model.getId())
                .identifier(model.getIdentifier())
                .identifierType(model.getIdentifierType())
                .passwordHash(model.getPasswordHash())
                .verificationToken(model.getVerificationToken())
                .verificationAttempts(model.getVerificationAttempts())
                .expiresAt(model.getExpiresAt())
                .lastSentAt(model.getLastSentAt())
                .verified(model.isVerified())
                .build();
    }

    /**
     * Converts JPA entity to domain model.
     */
    public UserRegistrationModel toDomain(UserRegistrationJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return reconstructFromEntity(entity);
    }

    private UserRegistrationModel reconstructFromEntity(UserRegistrationJpaEntity entity) {
        // Create model and manually set fields using reflection
        try {
            UserRegistrationModel model = UserRegistrationModel.builder()
                    .id(entity.getId())
                    .identifier(entity.getIdentifier())
                    .identifierType(entity.getIdentifierType())
                    .passwordHash(entity.getPasswordHash())
                    .expirationHours(24)
                    .build();

            // Use reflection to set additional fields
            var tokenField = UserRegistrationModel.class.getDeclaredField("verificationToken");
            tokenField.setAccessible(true);
            tokenField.set(model, entity.getVerificationToken());

            var attemptsField = UserRegistrationModel.class.getDeclaredField("verificationAttempts");
            attemptsField.setAccessible(true);
            attemptsField.set(model, entity.getVerificationAttempts());

            var expiresField = UserRegistrationModel.class.getDeclaredField("expiresAt");
            expiresField.setAccessible(true);
            expiresField.set(model, entity.getExpiresAt());

            var lastSentField = UserRegistrationModel.class.getDeclaredField("lastSentAt");
            lastSentField.setAccessible(true);
            lastSentField.set(model, entity.getLastSentAt());

            var verifiedField = UserRegistrationModel.class.getDeclaredField("verified");
            verifiedField.setAccessible(true);
            verifiedField.set(model, entity.getVerified() != null && entity.getVerified());

            return model;
        } catch (Exception e) {
            throw new RuntimeException("Failed to reconstruct UserRegistrationModel from entity", e);
        }
    }
}
