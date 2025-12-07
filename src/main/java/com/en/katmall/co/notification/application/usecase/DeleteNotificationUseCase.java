/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.notification.application.usecase;

import com.en.katmall.co.notification.domain.repository.NotificationRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Delete Notification
 * Deletes notifications for users.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeleteNotificationUseCase {

    private final NotificationRepository notificationRepository;

    /**
     * Deletes a single notification.
     * 
     * @param notificationId Notification ID
     * @throws ResourceNotFoundException if not found
     */
    public void execute(String notificationId) {
        Objects.requireNonNull(notificationId, "Notification ID must not be null");

        if (notificationRepository.findById(notificationId).isEmpty()) {
            throw new ResourceNotFoundException("Notification", notificationId);
        }
        notificationRepository.deleteById(notificationId);
        log.info("Deleted notification {}", notificationId);
    }

    /**
     * Deletes all notifications for a user.
     * 
     * @param userId User ID
     */
    public void executeAllForUser(String userId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        notificationRepository.deleteAllByUserId(userId);
        log.info("Deleted all notifications for user {}", userId);
    }
}
