/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.ordering.domain.repository;

import com.en.katmall.co.ordering.domain.model.Order;
import com.en.katmall.co.ordering.domain.model.valueobject.OrderNumber;
import com.en.katmall.co.ordering.domain.model.valueobject.OrderStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order aggregate root.
 * Defines the contract for order persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface OrderRepository {

    /**
     * Saves an order entity
     * 
     * @param order The order to save
     * @return The saved order
     */
    Order save(Order order);

    /**
     * Finds an order by ID
     * 
     * @param id The order ID
     * @return Optional containing the order if found
     */
    Optional<Order> findById(String id);

    /**
     * Finds an order by its order number
     * 
     * @param orderNumber The order number
     * @return Optional containing the order if found
     */
    Optional<Order> findByOrderNumber(OrderNumber orderNumber);

    /**
     * Finds all orders for a user
     * 
     * @param userId The user ID
     * @return List of orders for the user
     */
    List<Order> findByUserId(String userId);

    /**
     * Finds orders by user ID and status
     * 
     * @param userId The user ID
     * @param status The order status
     * @return List of matching orders
     */
    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);

    /**
     * Deletes an order
     * 
     * @param order The order to delete
     */
    void delete(Order order);
}
