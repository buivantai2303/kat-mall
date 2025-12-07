/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.notification.application.usecase;

import com.en.katmall.co.notification.domain.model.Notification;
import com.en.katmall.co.notification.domain.repository.NotificationRepository;
import com.en.katmall.co.shared.enums.KTypeNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Use Case: Send Notification
 * Sends various types of notifications to users.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SendNotificationUseCase {

    private final NotificationRepository notificationRepository;

    /**
     * Sends a system notification to a user.
     * 
     * @param userId  User ID
     * @param title   Notification title
     * @param message Notification message
     * @return Created notification
     */
    public Notification sendSystem(String userId, String title, String message) {
        Objects.requireNonNull(userId, "userId must not be null");

        Notification notification = Notification.systemNotification(userId, title, message);
        Notification saved = notificationRepository.save(notification);

        log.info("Sent system notification {} to user {}", saved.getId(), userId);
        return saved;
    }

    /**
     * Sends an order notification to a user.
     * 
     * @param userId  User ID
     * @param title   Notification title
     * @param message Notification message
     * @param orderId Related order ID
     * @return Created notification
     */
    public Notification sendOrderNotification(String userId, String title, String message, String orderId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(orderId, "orderId must not be null");

        Notification notification = Notification.orderNotification(userId, title, message, orderId);
        Notification saved = notificationRepository.save(notification);

        log.info("Sent order notification {} to user {} for order {}", saved.getId(), userId, orderId);
        return saved;
    }

    /**
     * Sends a promotion notification to a user.
     * 
     * @param userId      User ID
     * @param title       Notification title
     * @param message     Notification message
     * @param promotionId Related promotion ID
     * @return Created notification
     */
    public Notification sendPromoNotification(String userId, String title, String message, String promotionId) {
        Objects.requireNonNull(userId, "userId must not be null");

        Notification notification = Notification.promoNotification(userId, title, message, promotionId);
        Notification saved = notificationRepository.save(notification);

        log.info("Sent promo notification {} to user {}", saved.getId(), userId);
        return saved;
    }

    /**
     * Sends a security notification to a user.
     * 
     * @param userId  User ID
     * @param title   Notification title
     * @param message Notification message
     * @return Created notification
     */
    public Notification sendSecurityNotification(String userId, String title, String message) {
        Objects.requireNonNull(userId, "userId must not be null");

        Notification notification = Notification.securityNotification(userId, title, message);
        Notification saved = notificationRepository.save(notification);

        log.info("Sent security notification {} to user {}", saved.getId(), userId);
        return saved;
    }

    /**
     * Sends a notification to multiple users.
     * 
     * @param userIds List of user IDs
     * @param title   Notification title
     * @param message Notification message
     * @param type    Notification type
     * @return List of created notifications
     */
    public List<Notification> sendBulk(List<String> userIds, String title, String message, KTypeNotification type) {
        List<Notification> notifications = userIds.stream()
                .map(userId -> Notification.builder()
                        .userId(userId)
                        .title(title)
                        .message(message)
                        .type(type)
                        .build())
                .toList();

        List<Notification> saved = notificationRepository.saveAll(notifications);
        log.info("Sent bulk notification to {} users", userIds.size());
        return saved;
    }
}
