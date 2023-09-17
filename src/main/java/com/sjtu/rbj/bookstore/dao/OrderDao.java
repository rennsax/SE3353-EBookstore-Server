package com.sjtu.rbj.bookstore.dao;

import java.util.List;
import java.util.Optional;

import com.sjtu.rbj.bookstore.entity.Order;

/**
 * @author Bojun Ren
 * @data 2023/04/23
 */
public interface OrderDao {
    /**
     * Saves a given entity. Use the returned instance for further operations as the
     * save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity; will never be {@literal null}.
     */
    <S extends Order> S save(S entity);

    /**
     * Retrieves all order entities mapping to the target user.
     *
     * @param userId must not be {@literal null}
     * @return all order entities belonging to the target user.
     */
    List<Order> findByUserId(Integer userId);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none
     *         found.
     */
    Optional<Order> findById(Integer id);

    /**
     * Flushes all pending changes to the database.
     */
    void flush();

    /**
     * Find all entities.
     *
     * @return list of all entities.
     */
    List<Order> findAll();
}
