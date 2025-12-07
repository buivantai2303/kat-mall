/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.notification.application.usecase;

import com.en.katmall.co.notification.domain.model.Notification;
import com.en.katmall.co.notification.domain.repository.NotificationRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Use Case: Get Notification
 * Retrieves notifications for users.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetNotificationUseCase {

    private final NotificationRepository notificationRepository;

    /**
     * Finds a notification by ID.
     * 
     * @param id Notification ID
     * @return The notification
     * @throws ResourceNotFoundException if not found
     */
    public Notification executeById(String id) {
        Objects.requireNonNull(id, "Notification ID must not be null");
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));
    }

    /**
     * Finds notifications for a user with pagination.
     * 
     * @param userId User ID
     * @param page   Page number
     * @param size   Page size
     * @return List of notifications
     */
    public List<Notification> executeByUserId(String userId, int page, int size) {
        Objects.requireNonNull(userId, "User ID must not be null");
        return notificationRepository.findByUserId(userId, page, size);
    }

    /**
     * Finds unread notifications for a user.
     * 
     * @param userId User ID
     * @return List of unread notifications
     */
    public List<Notification> executeUnreadByUserId(String userId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        return notificationRepository.findUnreadByUserId(userId);
    }

    /**
     * Counts unread notifications for a user.
     * 
     * @param userId User ID
     * @return Unread count
     */
    public long countUnread(String userId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        return notificationRepository.countUnreadByUserId(userId);
    }
}
