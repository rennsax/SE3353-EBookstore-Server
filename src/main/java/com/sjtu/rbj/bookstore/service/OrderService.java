package com.sjtu.rbj.bookstore.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.sjtu.rbj.bookstore.dto.OrderInfoDTO;
import com.sjtu.rbj.bookstore.entity.Order;

/**
 * @author Bojun Ren
 */
public interface OrderService {
    /**
     * Get necessary order information by order id.
     *
     * @param orderId must not be {@literal null}.
     * @return OrderInfo
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     * @throws NoSuchElementException   if no such order.
     */
    OrderInfoDTO getOrderInfoByOrderId(Integer orderId);

    /**
     * Submit a "pending" order.
     * This process change the order state, and decrease corresponded book stock.
     *
     * @param orderId must not be {@literal null}.
     * @throws IllegalArgumentException      if {@literal id} is {@literal null}.
     * @throws NoSuchElementException        if no such order.
     * @throws UnsupportedOperationException if the target order isn't "pending".
     *
     */
    void submitOrder(Integer orderId);

    /**
     * Get all orders by user id (except the "pending" order).
     *
     * @param userId must not be {@literal null}.
     * @return all orders (except a "pending" order) belonging to the user.
     * @throws IllegalArgumentException if {@literal userId} is {@literal null}.
     */
    List<Order> getOrderByUserId(Integer userId);

    /**
     * Get all orders by user id (except the "pending" order).
     * The returned orders must contain book with the specified keyword.
     *
     * @param userId  must not be {@literal null}.
     * @param keyword must not be {@literal null}.
     * @return all orders (except a "pending" order) satisfying the conditions.
     * @throws IllegalArgumentException if {@literal userId} is {@literal null}.
     */
    List<Order> getOrderByUserId(Integer userId, String keyword);

    /**
     * Update the ordered item(s). Only "pending" orders' items can be updated.
     *
     * <p>
     * If given {@literal quantity} is positive, add ordered item(s).
     * Otherwise, <strong>try</strong> to delete items.
     * </p>
     *
     * @param orderId  must not be {@literal null}.
     * @param uuid     must not be {@literal null}.
     * @param quantity must not be {@literal null}.
     * @return true on successfully update the ordered item(s).
     *         A positive {@literal quantity} always leads to a {@code true}
     *         returned value.
     * @throws IllegalArgumentException      if any of the parameters is
     *                                       {@literal null}.
     * @throws NoSuchElementException        if no such order or no such book.
     * @throws UnsupportedOperationException if the target order isn't "pending".
     */
    Boolean updateOrder(Integer orderId, UUID uuid, Integer quantity);

    /**
     * Get all orders (not pending).
     *
     * @return list of orders.
     */
    List<Order> getAllOrders();

    /**
     * Get all orders (not pending, and contains the keyword).
     *
     * @param keyword must not be {@literal null}.
     * @return lists of orders.
     */
    List<Order> getAllOrders(String keyword);

    /**
     * Filter orders by begin time and end time.
     *
     * @param orderList list to be filtered.
     * @param beginTime begin time in unix timestamp
     * @param endTime   end time in unix timestamp
     * @return order list after filtered.
     */
    @Deprecated
    List<Order> filterByBeginAndEnd(List<Order> orderList, Long beginTime, Long endTime);
}
