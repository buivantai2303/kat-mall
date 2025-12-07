/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.notification.domain.repository;

import com.en.katmall.co.notification.domain.model.Notification;
import com.en.katmall.co.shared.enums.KTypeNotification;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Notification aggregate root.
 * Defines the contract for notification persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface NotificationRepository {

    /**
     * Saves a notification
     * 
     * @param notification The notification to save
     * @return The saved notification
     */
    Notification save(Notification notification);

    /**
     * Saves multiple notifications
     * 
     * @param notifications The notifications to save
     * @return List of saved notifications
     */
    List<Notification> saveAll(List<Notification> notifications);

    /**
     * Finds notification by ID
     * 
     * @param id The notification ID
     * @return Optional containing the notification if found
     */
    Optional<Notification> findById(String id);

    /**
     * Finds all notifications for a user
     * 
     * @param userId The user ID
     * @param page   Page number (0-indexed)
     * @param size   Page size
     * @return List of notifications for the user
     */
    List<Notification> findByUserId(String userId, int page, int size);

    /**
     * Finds unread notifications for a user
     * 
     * @param userId The user ID
     * @return List of unread notifications
     */
    List<Notification> findUnreadByUserId(String userId);

    /**
     * Finds notifications by type for a user
     * 
     * @param userId The user ID
     * @param type   The notification type
     * @return List of notifications of the specified type
     */
    List<Notification> findByUserIdAndType(String userId, KTypeNotification type);

    /**
     * Counts unread notifications for a user
     * 
     * @param userId The user ID
     * @return Number of unread notifications
     */
    long countUnreadByUserId(String userId);

    /**
     * Marks all notifications as read for a user
     * 
     * @param userId The user ID
     */
    void markAllAsRead(String userId);

    /**
     * Deletes a notification by ID
     * 
     * @param id The notification ID to delete
     */
    void deleteById(String id);

    /**
     * Deletes all notifications for a user
     * 
     * @param userId The user ID
     */
    void deleteAllByUserId(String userId);
}
