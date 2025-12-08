/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.ordering.infrastructure.persistence.repository;

import com.en.katmall.co.ordering.domain.model.Order;
import com.en.katmall.co.ordering.domain.model.valueobject.OrderNumber;
import com.en.katmall.co.ordering.domain.model.valueobject.OrderStatus;
import com.en.katmall.co.ordering.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of OrderRepository.
 * TODO: Replace with JPA implementation
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final Map<String, Order> storage = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        Objects.requireNonNull(order, "Order must not be null");
        storage.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Order> findByOrderNumber(OrderNumber orderNumber) {
        return storage.values().stream()
                .filter(o -> o.getOrderNumber() != null && o.getOrderNumber().equals(orderNumber))
                .findFirst();
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return storage.values().stream()
                .filter(o -> userId.equals(o.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByUserIdAndStatus(String userId, OrderStatus status) {
        return storage.values().stream()
                .filter(o -> userId.equals(o.getUserId()) && status.equals(o.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Order order) {
        storage.remove(order.getId());
    }
}
