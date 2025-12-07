/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.interfaces.rest;

import com.en.katmall.co.identity.application.dto.request.QuickRegisterRequest;
import com.en.katmall.co.identity.application.dto.request.ResendVerificationRequest;
import com.en.katmall.co.identity.application.dto.response.PendingRegistrationResponse;
import com.en.katmall.co.identity.application.dto.response.VerificationResponse;
import com.en.katmall.co.identity.application.usecase.RegisterUseCase;
import com.en.katmall.co.identity.application.usecase.ResendVerificationUseCase;
import com.en.katmall.co.identity.application.usecase.VerifyRegistrationUseCase;
import com.en.katmall.co.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for user registration endpoints.
 * Delegates to individual use cases for registration operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegisterUseCase registerUseCase;
    private final VerifyRegistrationUseCase verifyRegistrationUseCase;
    private final ResendVerificationUseCase resendVerificationUseCase;

    /**
     * Registers a new user with email/phone verification.
     * 
     * @param request Registration request
     * @return Pending registration response
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<PendingRegistrationResponse>> register(
            @Valid @RequestBody QuickRegisterRequest request) {
        PendingRegistrationResponse response = registerUseCase.execute(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Registration successful. Please verify your email."));
    }

    /**
     * Verifies a registration using the verification token.
     * 
     * @param token Verification token from email
     * @return Verification response
     */
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<VerificationResponse>> verify(
            @RequestParam String token) {
        VerificationResponse response = verifyRegistrationUseCase.execute(token);
        return ResponseEntity.ok(ApiResponse.success(response, "Account verified successfully."));
    }

    /**
     * Resends verification email/SMS.
     * 
     * @param request Resend request
     * @return Updated pending registration response
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<PendingRegistrationResponse>> resendVerification(
            @Valid @RequestBody ResendVerificationRequest request) {
        PendingRegistrationResponse response = resendVerificationUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Verification email resent."));
    }
}
