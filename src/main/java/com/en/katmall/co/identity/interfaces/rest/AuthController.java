/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.interfaces.rest;

import com.en.katmall.co.identity.application.dto.request.*;
import com.en.katmall.co.identity.application.dto.response.AuthResponse;
import com.en.katmall.co.identity.application.dto.response.UserResponse;
import com.en.katmall.co.identity.application.usecase.*;
import com.en.katmall.co.shared.dto.ApiResponse;
import com.en.katmall.co.shared.infrastructure.i18n.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication operations.
 * Delegates to individual use cases for each operation.
 * 
 * Note: Registration is handled by RegistrationController with email
 * verification.
 *
 * @author tai.buivan
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String MSG_LOGIN_SUCCESS = "auth.login.success";
    private static final String MSG_LOGOUT_SUCCESS = "auth.logout.success";
    private static final String MSG_CHANGE_PASSWORD = "auth.change.password";
    private static final String MSG_UPDATE_SUCCESS = "userModel.update.success";

    // Use Cases
    private final LoginUseCase loginUseCase;
    private final SocialLoginUseCase socialLoginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final MessageService messageService;

    /**
     * Authenticates user with email and password
     * POST /api/v1/auth/login
     *
     * @param request Login credentials
     * @return Auth response with tokens
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = loginUseCase.execute(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, messageService.get(MSG_LOGIN_SUCCESS)));
    }

    /**
     * Authenticates or registers user via social provider
     * POST /api/v1/auth/social
     *
     * @param request Social login data
     * @return Auth response with tokens
     */
    @PostMapping("/social")
    public ResponseEntity<ApiResponse<AuthResponse>> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
        AuthResponse response = socialLoginUseCase.execute(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, messageService.get(MSG_LOGIN_SUCCESS)));
    }

    /**
     * Refreshes access token using refresh token
     * POST /api/v1/auth/refresh
     *
     * @param request Refresh token
     * @return New auth response with tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = refreshTokenUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Gets current authenticated user's profile
     * GET /api/v1/auth/me
     *
     * @return User profile
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UserResponse response = getCurrentUserUseCase.execute();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Updates current user's profile
     * PUT /api/v1/auth/me
     *
     * @param request Profile update data
     * @return Updated user profile
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UserResponse response = updateProfileUseCase.execute(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, messageService.get(MSG_UPDATE_SUCCESS)));
    }

    /**
     * Changes current user's password
     * POST /api/v1/auth/change-password
     *
     * @param request Password change data
     * @return Success message
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(request);
        return ResponseEntity.ok(
                ApiResponse.success(messageService.get(MSG_CHANGE_PASSWORD)));
    }

    /**
     * Logs out current user (client-side token invalidation)
     * POST /api/v1/auth/logout
     *
     * @return Success message
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // JWT tokens are stateless, logout is handled client-side
        // Optionally: add token to blacklist for server-side invalidation
        return ResponseEntity.ok(
                ApiResponse.success(messageService.get(MSG_LOGOUT_SUCCESS)));
    }
}
