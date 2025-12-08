/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.usecase;

import com.en.katmall.co.identity.application.dto.request.ChangePasswordRequest;
import com.en.katmall.co.identity.domain.model.UserModel;
import com.en.katmall.co.identity.domain.repository.UserRepository;
import com.en.katmall.co.shared.exception.DomainException;
import com.en.katmall.co.shared.exception.NotFoundException;
import com.en.katmall.co.shared.exception.ValidationException;
import com.en.katmall.co.shared.infrastructure.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Change Password
 * Allows authenticated user to change their password.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Executes the change password use case.
     * Password matching is validated by @KFieldsMatch annotation on DTO.
     * 
     * @param request Change password data
     */
    @Transactional
    public void execute(ChangePasswordRequest request) {
        Objects.requireNonNull(request, "Change password request must not be null");

        AuthenticatedUser auth = getAuthenticatedUser();
        UserModel userModel = userRepository.findById(auth.getId())
                .orElseThrow(() -> new NotFoundException("User", auth.getId()));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), userModel.getPasswordHash())) {
            throw new ValidationException("currentPassword", "k.validation.password.incorrect");
        }

        // Update password
        userModel.changePassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(userModel);
    }

    private AuthenticatedUser getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if (principal instanceof AuthenticatedUser) {
            return (AuthenticatedUser) principal;
        }
        throw new DomainException("UNAUTHORIZED", "k.error.unauthorized");
    }
}
