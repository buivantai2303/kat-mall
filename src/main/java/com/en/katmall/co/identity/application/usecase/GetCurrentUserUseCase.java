/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.usecase;

import com.en.katmall.co.identity.application.dto.response.UserResponse;
import com.en.katmall.co.identity.domain.model.UserModel;
import com.en.katmall.co.identity.domain.repository.UserRepository;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.exception.NotFoundException;
import com.en.katmall.co.shared.infrastructure.i18n.MessageService;
import com.en.katmall.co.shared.infrastructure.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case: Get Current User Profile
 * Returns the authenticated user's profile information.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class GetCurrentUserUseCase {

    private static final String ERROR_UNAUTHORIZED = "UNAUTHORIZED";
    private static final String MSG_UNAUTHORIZED = "auth.unauthorized";

    private final UserRepository userRepository;
    private final MessageService messageService;

    /**
     * Executes the get current user use case
     * 
     * @return User profile response
     */
    @Transactional(readOnly = true)
    public UserResponse execute() {
        AuthenticatedUser auth = getAuthenticatedUser();
        UserModel userModel = userRepository.findById(auth.getId())
                .orElseThrow(() -> new NotFoundException("User", auth.getId()));
        return mapToUserResponse(userModel);
    }

    private AuthenticatedUser getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if (principal instanceof AuthenticatedUser) {
            return (AuthenticatedUser) principal;
        }
        throw new DomainException(ERROR_UNAUTHORIZED, messageService.get(MSG_UNAUTHORIZED));
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
