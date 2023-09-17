package com.sjtu.rbj.bookstore.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sjtu.rbj.bookstore.dao.OrderDao;
import com.sjtu.rbj.bookstore.entity.Order;
import com.sjtu.rbj.bookstore.repository.OrderRepository;

/**
 * @author Bojun Ren
 */
@Repository
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public <S extends Order> S save(S entity) {
        return orderRepository.save(entity);
    }

    @Override
    public List<Order> findByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Optional<Order> findById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public void flush() {
        orderRepository.flush();
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

}
