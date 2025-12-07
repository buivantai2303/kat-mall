/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.notification.domain.model;

import com.en.katmall.co.shared.domain.AggregateRoot;
import com.en.katmall.co.shared.enums.KTypeNotification;
import com.en.katmall.co.shared.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Notification aggregate root for user notifications.
 * Handles system notifications, order updates, promotions, and security alerts.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class Notification extends AggregateRoot<String> {

    private String userId;
    private String title;
    private String message;
    private KTypeNotification type;
    private String referenceId;
    private boolean isRead;

    /** Default constructor for JPA */
    protected Notification() {
        super();
    }

    /**
     * Private constructor for Builder pattern
     */
    private Notification(Builder builder) {
        super(builder.id != null ? builder.id : IdGenerator.generate());
        this.userId = Objects.requireNonNull(builder.userId, "userId must not be null");
        this.title = Objects.requireNonNull(builder.title, "title must not be null");
        this.message = builder.message;
        this.type = builder.type != null ? builder.type : KTypeNotification.SYSTEM;
        this.referenceId = builder.referenceId;
        this.isRead = false;
    }

    /**
     * Creates a new builder for Notification
     * 
     * @return New Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Factory method to create a system notification
     * 
     * @param userId  The user ID
     * @param title   Notification title
     * @param message Notification message
     * @return New system notification
     */
    public static Notification systemNotification(String userId, String title, String message) {
        return builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(KTypeNotification.SYSTEM)
                .build();
    }

    /**
     * Factory method to create an order notification
     * 
     * @param userId  The user ID
     * @param title   Notification title
     * @param message Notification message
     * @param orderId The related order ID
     * @return New order notification
     */
    public static Notification orderNotification(String userId, String title, String message, String orderId) {
        return builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(KTypeNotification.ORDER)
                .referenceId(orderId)
                .build();
    }

    /**
     * Factory method to create a promotion notification
     * 
     * @param userId      The user ID
     * @param title       Notification title
     * @param message     Notification message
     * @param promotionId The related promotion ID
     * @return New promotion notification
     */
    public static Notification promoNotification(String userId, String title, String message, String promotionId) {
        return builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(KTypeNotification.PROMOTION)
                .referenceId(promotionId)
                .build();
    }

    /**
     * Factory method to create an account security notification
     * 
     * @param userId  The user ID
     * @param title   Notification title
     * @param message Notification message
     * @return New account notification
     */
    public static Notification securityNotification(String userId, String title, String message) {
        return builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(KTypeNotification.ACCOUNT)
                .build();
    }

    /**
     * Marks this notification as read
     */
    public void markAsRead() {
        if (!this.isRead) {
            this.isRead = true;
            markAsUpdated();
        }
    }

    /**
     * Marks this notification as unread
     */
    public void markAsUnread() {
        if (this.isRead) {
            this.isRead = false;
            markAsUpdated();
        }
    }

    /**
     * Updates the notification message
     * 
     * @param title   New title
     * @param message New message
     */
    public void updateContent(String title, String message) {
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.message = message;
        markAsUpdated();
    }

    /**
     * Builder class for Notification
     */
    public static class Builder {
        private String id;
        private String userId;
        private String title;
        private String message;
        private KTypeNotification type;
        private String referenceId;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder type(KTypeNotification type) {
            this.type = type;
            return this;
        }

        public Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        /**
         * Builds the Notification instance
         * 
         * @return New Notification instance
         * @throws NullPointerException if required fields are missing
         */
        public Notification build() {
            return new Notification(this);
        }
    }
}
