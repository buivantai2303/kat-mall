/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Standard API response wrapper.
 * Provides consistent response structure for all API endpoints.
 * 
 * @param <T> Type of the response data
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /** Indicates if the request was successful */
    private boolean success;

    /** Response message (localized) */
    private String message;

    /** Response data payload */
    private T data;

    /** Timestamp of the response */
    @Builder.Default
    private Instant timestamp = Instant.now();

    /** Error code if applicable */
    private String errorCode;


    /**
     * Creates a success response with data
     * 
     * @param data The response data
     * @param <T>  Type of data
     * @return ApiResponse with success=true
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    /**
     * Creates a success response with data and message
     * 
     * @param data    The response data
     * @param message Success message
     * @param <T>     Type of data
     * @return ApiResponse with success=true
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    /**
     * Creates a success response with message only
     * 
     * @param message Success message
     * @return ApiResponse with success=true
     */
    public static ApiResponse<Void> success(String message) {
        return ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .build();
    }

    /**
     * Creates an error response
     * 
     * @param message Error message
     * @param <T>     Type of data
     * @return ApiResponse with success=false
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    /**
     * Creates an error response with error code
     * 
     * @param message   Error message
     * @param errorCode Error code
     * @param <T>       Type of data
     * @return ApiResponse with success=false
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }
}
