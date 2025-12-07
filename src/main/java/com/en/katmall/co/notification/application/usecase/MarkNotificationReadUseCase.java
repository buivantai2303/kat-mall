/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.notification.application.usecase;

import com.en.katmall.co.notification.domain.model.Notification;
import com.en.katmall.co.notification.domain.repository.NotificationRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Mark Notification As Read
 * Marks notifications as read for users.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MarkNotificationReadUseCase {

    private final NotificationRepository notificationRepository;

    /**
     * Marks a single notification as read.
     * 
     * @param notificationId Notification ID
     * @return Updated notification
     * @throws ResourceNotFoundException if not found
     */
    public Notification execute(String notificationId) {
        Objects.requireNonNull(notificationId, "Notification ID must not be null");

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", notificationId));

        notification.markAsRead();
        return notificationRepository.save(notification);
    }

    /**
     * Marks all notifications as read for a user.
     * 
     * @param userId User ID
     */
    public void executeAll(String userId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        notificationRepository.markAllAsRead(userId);
        log.info("Marked all notifications as read for user {}", userId);
    }
}
