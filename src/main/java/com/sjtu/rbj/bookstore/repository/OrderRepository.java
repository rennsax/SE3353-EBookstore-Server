package com.sjtu.rbj.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sjtu.rbj.bookstore.entity.Order;

/**
 * @author Bojun Ren
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {
    /**
     * Retrieves all order entities mapping to the target user.
     *
     * @param userId must not be {@literal null}
     * @return all order entities belonging to the target user.
     * @throws IllegalArgumentException if {@literal userId} is {@literal null}.
     */
    List<Order> findByUserId(Integer userId);
}
