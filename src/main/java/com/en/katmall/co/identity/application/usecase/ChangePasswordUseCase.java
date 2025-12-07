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
import com.en.katmall.co.shared.infrastructure.i18n.MessageService;
import com.en.katmall.co.shared.infrastructure.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
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

    private static final String ERROR_UNAUTHORIZED = "UNAUTHORIZED";
    private static final String MSG_UNAUTHORIZED = "auth.unauthorized";
    private static final String MSG_PASSWORD_MISMATCH = "validation.password.mismatch";
    private static final String MSG_PASSWORD_INCORRECT = "auth.password.incorrect";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;

    /**
     * Executes the change password use case
     * 
     * @param request Change password data
     */
    @Transactional
    public void execute(ChangePasswordRequest request) {
        Objects.requireNonNull(request, "Change password request must not be null");

        // Validate password confirmation
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new ValidationException(
                    messageService.get(MSG_PASSWORD_MISMATCH),
                    Map.of("confirmNewPassword", messageService.get(MSG_PASSWORD_MISMATCH)));
        }

        AuthenticatedUser auth = getAuthenticatedUser();
        UserModel userModel = userRepository.findById(auth.getId())
                .orElseThrow(() -> new NotFoundException("User", auth.getId()));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), userModel.getPasswordHash())) {
            throw new ValidationException(
                    messageService.get(MSG_PASSWORD_INCORRECT),
                    Map.of("currentPassword", messageService.get(MSG_PASSWORD_INCORRECT)));
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
        throw new DomainException(ERROR_UNAUTHORIZED, messageService.get(MSG_UNAUTHORIZED));
    }
}
