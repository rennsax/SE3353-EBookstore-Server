package com.sjtu.rbj.bookstore.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sjtu.rbj.bookstore.entity.Book;

/**
 * @author Bojun Ren
 */
public interface BookDao {

    /**
     * Retrieves book entities, with maximum entity number and a certain offset.
     *
     * @param limit  the maximum entity number, must not be {@literal null}.
     * @param offset must not be {@literal null}.
     * @return available entities no more than {@literal limit}.
     */
    List<Book> findWithLimitWithOffset(Integer limit, Integer offset);

    /**
     * Retrieves a book entity by its uuid (defined as unique key).
     *
     * @param uuid must not be {@literal null}.
     * @return the entity with the given uuid or {@literal Optional#empty()} if none
     *         found.
     */
    Optional<Book> findByUuid(UUID uuid);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none
     *         found.
     */
    Optional<Book> findById(Integer id);

    /**
     * Returns all instances of books.
     *
     * @return all entities
     */
    List<Book> findAll();

    /**
     * Delete a book by its uuid.
     *
     * @param uuid
     */
    void deleteByUuid(UUID uuid);

    /**
     * Search by the book title.
     *
     * @param like
     * @return results.
     */
    List<Book> findByTitleLike(String like);

    /**
     * Save a book entity.
     *
     * @param <S>    derived class of {@literal com.sjtu.rbj.bookstore.entity.Book}
     * @param entity the field {@literal title} must not be {@literal null},
     *               otherwise an exception will be thrown.
     * @return the managed entity.
     */
    <S extends Book> S save(S entity);
}
