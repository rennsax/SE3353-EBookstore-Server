package com.sjtu.rbj.bookstore.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sjtu.rbj.bookstore.entity.Book;

/**
 * @author Bojun Ren
 */
public interface BookRepository extends JpaRepository<Book, Integer> {

    /**
     * Retrieves a book entity by its uuid (defined as unique key).
     *
     * @param uuid must not be {@literal null}.
     * @return the entity with the given uuid or {@literal Optional#empty()} if none
     *         found.
     * @throws IllegalArgumentException if {@literal uuid} is {@literal null}.
     */
    Optional<Book> findByUuid(UUID uuid);

    /**
     * Retrieves book entities, with maximum entity number and a certain offset.
     *
     * @param limit  the maximum entity number, must not be {@literal null}.
     * @param offset must not be {@literal null}.
     * @return available entities no more than {@literal limit}.
     * @throws IllegalArgumentException if either {@literal limit} or
     *                                  {@literal offset} is {@literal null}.
     */
    @Query(value = "select id, author, date, description, isbn, pic_id, 100*price as price, title, uuid, stock from `book` limit ?1 offset ?2", nativeQuery = true)
    List<Book> findWithLimitWithOffset(Integer limit, Integer offset);

    /**
     * Delete a book entity by its uuid.
     * @param uuid
     */
    void deleteByUuid(UUID uuid);

    /**
     * Search by the book title.
     * @param like
     * @return results.
     */
    List<Book> findByTitleLike(String like);
}
