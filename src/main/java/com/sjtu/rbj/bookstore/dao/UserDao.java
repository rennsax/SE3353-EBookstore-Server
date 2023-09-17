package com.sjtu.rbj.bookstore.dao;

import java.util.List;
import java.util.Optional;

import com.sjtu.rbj.bookstore.entity.User;

/**
 * @author Bojun Ren
 * @date 2023/04/18
 */
public interface UserDao {

    /**
     * Returns all instances of user entity.
     *
     * @return all user entities.
     */
    List<User> findAll();

    /**
     * Find the user entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return an {@code Optional} object may contain the user.
     */
    Optional<User> findById(Integer id);

    /**
     * Retrieves a user entity by its account (defined as unique key).
     *
     * @param account must not be {@literal null}.
     * @return the entity with the given account or {@literal Optional#empty()} if
     *         none found.
     */
    Optional<User> findByAccount(String account);

    /**
     * Retrieves a user entity by its account (defined as unique key) and passwd.
     *
     * @param account must not be {@literal null}.
     * @param passwd  must not be {@literal null}.
     * @return the entity with the given account or {@literal Optional#empty()} if
     *         none found.
     */
    Optional<User> findByAccountAndPasswd(String account, String passwd);

    /**
     * Flushes all pending changes to the database.
     */
    void flush();

    /**
     * Saves a given entity. Use the returned instance for further operations as the
     * save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity; will never be {@literal null}.
     */
    <S extends User> S save(S entity);
}
