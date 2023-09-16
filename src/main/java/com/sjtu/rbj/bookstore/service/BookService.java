package com.sjtu.rbj.bookstore.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.sjtu.rbj.bookstore.dto.BookDTO;
import com.sjtu.rbj.bookstore.entity.Book;

/**
 * @author Bojun Ren
 * @date 2023/04/19
 */
public interface BookService {

    /**
     * Get book information for homepage, from the top of the database
     *
     * @param limit the maximum number of books to get.
     *        Constraints: {@code 1 <= limit <= 20}. Otherwise, it's forced to be resized.
     * @return {@code List<Book>} with size no more than {@code limit}
     */
    List<Book> getBookListForHomePage(Integer limit);

    /**
     * Get book information for homepage, from the top of the database
     * @param limit the maximum number of books to get
     * @param offset the beginning row number to get book information
     * @return {@code List<Book>}
     */
    List<Book> getBookDataListForHomePage(Integer limit, Integer offset);

    /**
     * Get a book's info by uuid.
     *
     * @param uuid must not be {@literal null}.
     * @return the book entity.
     * @throws NoSuchElementException if a book cannot be found.
     */
    Book getBookByUuid(UUID uuid);

    /**
     * Get all books from the database.
     * @return list of book entities.
     */
    List<Book> getAllBooks();

    /**
     * Update the book.
     * @param bookDTO
     */
    void updateBook(BookDTO bookDTO);


    /**
     * Delete the book from database.
     * @param uuid must not be {@literal null}.
     * @throws NoSuchElementException if the book to be deleted can't be found.
     */
    void deleteBookByUuid(UUID uuid);


    /**
     * Find books by title, case insensitive.
     * @param keyword must not be {@literal null}.
     * @return list of found results.
     */
    List<Book> findBookByTitle(String keyword);


    /**
     * Add a book entity.
     *
     * @param book the field {@literal title} must not be {@literal null}.
     * @return the managed book entity.
     */
    Book addBook(Book book);
}
