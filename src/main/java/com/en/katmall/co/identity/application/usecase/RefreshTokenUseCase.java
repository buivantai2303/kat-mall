/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.usecase;

import com.en.katmall.co.identity.application.dto.request.RefreshTokenRequest;
import com.en.katmall.co.identity.application.dto.response.AuthResponse;
import com.en.katmall.co.identity.application.dto.response.UserResponse;
import com.en.katmall.co.identity.domain.model.UserModel;
import com.en.katmall.co.identity.domain.repository.UserRepository;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.exception.NotFoundException;
import com.en.katmall.co.shared.infrastructure.i18n.MessageService;
import com.en.katmall.co.shared.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Refresh Access Token
 * Generates new access token using refresh token.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private static final String ERROR_INVALID_TOKEN = "INVALID_TOKEN";
    private static final String MSG_SESSION_EXPIRED = "auth.session.expired";
    private static final String TOKEN_TYPE_BEARER = "Bearer";

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageService messageService;

    /**
     * Executes the refresh token use case
     * 
     * @param request Refresh token request
     * @return New auth response with tokens
     */
    @Transactional
    public AuthResponse execute(RefreshTokenRequest request) {
        Objects.requireNonNull(request, "Refresh token request must not be null");

        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new DomainException(
                    ERROR_INVALID_TOKEN,
                    messageService.get(MSG_SESSION_EXPIRED));
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));

        return createAuthResponse(userModel);
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
