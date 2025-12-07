/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.usecase;

import com.en.katmall.co.identity.application.dto.request.LoginRequest;
import com.en.katmall.co.identity.application.dto.response.AuthResponse;
import com.en.katmall.co.identity.application.dto.response.UserResponse;
import com.en.katmall.co.identity.domain.model.UserModel;
import com.en.katmall.co.identity.domain.model.valueobject.Email;
import com.en.katmall.co.identity.domain.repository.UserRepository;
import com.en.katmall.co.shared.enums.KTypeUserStatus;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.exception.ValidationException;
import com.en.katmall.co.shared.infrastructure.i18n.MessageService;
import com.en.katmall.co.shared.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

/**
 * Use Case: User Login
 * Authenticates user with email and password.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private static final String ERROR_ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
    private static final String ERROR_ACCOUNT_INACTIVE = "ACCOUNT_INACTIVE";

    private static final String MSG_LOGIN_FAILED = "auth.login.failed";
    private static final String MSG_ACCOUNT_LOCKED = "auth.account.locked";
    private static final String MSG_ACCOUNT_DISABLED = "auth.account.disabled";
    private static final String TOKEN_TYPE_BEARER = "Bearer";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageService messageService;

    /**
     * Executes the login use case
     * 
     * @param request Login credentials
     * @return Auth response with tokens
     */
    @Transactional
    public AuthResponse execute(LoginRequest request) {
        Objects.requireNonNull(request, "Login request must not be null");

        Email email = Email.of(request.getEmail());

        UserModel userModel = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException(
                        messageService.get(MSG_LOGIN_FAILED),
                        Map.of("email", messageService.get(MSG_LOGIN_FAILED))));

        // Check if account is locked
        if (userModel.getStatus() == KTypeUserStatus.LOCKED) {
            throw new DomainException(
                    ERROR_ACCOUNT_LOCKED,
                    messageService.get(MSG_ACCOUNT_LOCKED));
        }

        // Check if account is inactive
        if (userModel.getStatus() == KTypeUserStatus.INACTIVE) {
            throw new DomainException(
                    ERROR_ACCOUNT_INACTIVE,
                    messageService.get(MSG_ACCOUNT_DISABLED));
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), userModel.getPasswordHash())) {
            throw new ValidationException(
                    messageService.get(MSG_LOGIN_FAILED),
                    Map.of("password", messageService.get(MSG_LOGIN_FAILED)));
        }

        // Update last login
        userModel.recordLogin();
        userRepository.save(userModel);

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
