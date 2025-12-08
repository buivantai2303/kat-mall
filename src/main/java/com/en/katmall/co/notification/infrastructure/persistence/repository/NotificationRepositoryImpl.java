/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.notification.infrastructure.persistence.repository;

import com.en.katmall.co.notification.domain.model.Notification;
import com.en.katmall.co.notification.domain.repository.NotificationRepository;
import com.en.katmall.co.shared.enums.KTypeNotification;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of NotificationRepository.
 * TODO: Replace with JPA implementation
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final Map<String, Notification> storage = new ConcurrentHashMap<>();

    @Override
    public Notification save(Notification notification) {
        Objects.requireNonNull(notification, "Notification must not be null");
        storage.put(notification.getId(), notification);
        return notification;
    }

    @Override
    public List<Notification> saveAll(List<Notification> notifications) {
        notifications.forEach(n -> storage.put(n.getId(), n));
        return notifications;
    }

    @Override
    public Optional<Notification> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Notification> findByUserId(String userId, int page, int size) {
        return storage.values().stream()
                .filter(n -> userId.equals(n.getUserId()))
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findUnreadByUserId(String userId) {
        return storage.values().stream()
                .filter(n -> userId.equals(n.getUserId()) && !n.isRead())
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByUserIdAndType(String userId, KTypeNotification type) {
        return storage.values().stream()
                .filter(n -> userId.equals(n.getUserId()) && type.equals(n.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public long countUnreadByUserId(String userId) {
        return storage.values().stream()
                .filter(n -> userId.equals(n.getUserId()) && !n.isRead())
                .count();
    }

    @Override
    public void markAllAsRead(String userId) {
        storage.values().stream()
                .filter(n -> userId.equals(n.getUserId()))
                .forEach(Notification::markAsRead);
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    @Override
    public void deleteAllByUserId(String userId) {
        storage.values().removeIf(n -> userId.equals(n.getUserId()));
    }
}
