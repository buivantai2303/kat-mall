/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.usecase;

import com.en.katmall.co.identity.application.dto.request.SocialLoginRequest;
import com.en.katmall.co.identity.application.dto.response.AuthResponse;
import com.en.katmall.co.identity.application.dto.response.UserResponse;
import com.en.katmall.co.identity.domain.model.UserModel;
import com.en.katmall.co.identity.domain.model.valueobject.Email;
import com.en.katmall.co.identity.domain.repository.UserRepository;
import com.en.katmall.co.shared.enums.KTypeAuthProvider;
import com.en.katmall.co.shared.exception.ValidationException;
import com.en.katmall.co.shared.infrastructure.i18n.MessageService;
import com.en.katmall.co.shared.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Use Case: Social Login
 * Authenticates or registers user via social provider (Google, Facebook,
 * Apple).
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class SocialLoginUseCase {

    private static final String MSG_UNSUPPORTED_PROVIDER = "validation.unsupported.provider";
    private static final String TOKEN_TYPE_BEARER = "Bearer";

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageService messageService;

    /**
     * Executes the social login use case
     * 
     * @param request Social login data
     * @return Auth response with tokens
     */
    @Transactional
    public AuthResponse execute(SocialLoginRequest request) {
        Objects.requireNonNull(request, "Social login request must not be null");

        // Validate provider
        KTypeAuthProvider provider = KTypeAuthProvider.fromCode(request.getProvider())
                .orElseThrow(() -> new ValidationException(
                        messageService.get(MSG_UNSUPPORTED_PROVIDER),
                        Map.of("provider", messageService.get(MSG_UNSUPPORTED_PROVIDER))));

        // TODO: Verify token with social provider (Google, Facebook, Apple)
        // For now, trust the token and use provided data

        Email email = Email.of(request.getEmail());

        // Find existing user or create new
        Optional<UserModel> existingUser = userRepository.findByEmail(email);
        UserModel userModel;

        if (existingUser.isPresent()) {
            userModel = existingUser.get();
            // Update social provider info
            userModel.linkSocialProvider(provider, request.getProviderId());
            if (request.getAvatarUrl() != null) {
                userModel.updateAvatar(request.getAvatarUrl());
            }
        } else {
            // Create new user from social data
            userModel = UserModel.createFromSocialProvider(
                    request.getName(),
                    email,
                    provider,
                    request.getProviderId());
            if (request.getAvatarUrl() != null) {
                userModel.updateAvatar(request.getAvatarUrl());
            }
        }

        UserModel savedUserModel = userRepository.save(userModel);
        return createAuthResponse(savedUserModel);
    }

    private AuthResponse createAuthResponse(UserModel userModel) {
        String accessToken = jwtTokenProvider.generateAccessToken(
                userModel.getId(),
                userModel.getEmail().getValue(),
                userModel.getRole().getCode());
        String refreshToken = jwtTokenProvider.generateRefreshToken(
                userModel.getId(),
                userModel.getEmail().getValue());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TOKEN_TYPE_BEARER)
                .expiresIn(jwtTokenProvider.getAccessTokenExpirationSeconds())
                .user(mapToUserResponse(userModel))
                .build();
    }

    private UserResponse mapToUserResponse(UserModel userModel) {
        return UserResponse.builder()
                .id(userModel.getId())
                .email(userModel.getEmail().getValue())
                .fullName(userModel.getFullName())
                .phone(userModel.getPhone())
                .avatarUrl(userModel.getAvatarUrl())
                .emailVerified(userModel.isEmailVerified())
                .role(userModel.getRole().getCode())
                .createdAt(userModel.getCreatedAt())
                .lastLoginAt(userModel.getLastLoginAt())
                .build();
    }
}
