package com.sjtu.rbj.bookstore.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjtu.rbj.bookstore.dao.BookDao;
import com.sjtu.rbj.bookstore.dto.BookDTO;
import com.sjtu.rbj.bookstore.entity.Book;
import com.sjtu.rbj.bookstore.service.BookService;

/**
 * @author Bojun Ren
 * @date 2023/04/19
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Override
    public Book getBookByUuid(UUID uuid) {
        Optional<Book> maybeBook = bookDao.findByUuid(uuid);
        Book book = maybeBook
                .orElseThrow(() -> new NoSuchElementException("cannot find such book!"));
        return book;
    }

    @Override
    public List<Book> getBookListForHomePage(Integer limit) {
        return this.getBookDataListForHomePage(limit, 0);
    }

    @Override
    public List<Book> getBookDataListForHomePage(Integer limit, Integer offset) {
        /** resize limit */
        limit = Math.max(limit, 1);
        limit = Math.min(20, limit);
        List<Book> bookList = bookDao.findWithLimitWithOffset(limit, offset);
        return bookList;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBook(BookDTO bookDTO) {
        Optional<Book> maybeBook = bookDao.findByUuid(bookDTO.getUuid());
        Book book = maybeBook
                .orElseThrow(() -> new NoSuchElementException("Can't find such book!"));
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setStock(bookDTO.getStock());
        book.setPicId(bookDTO.getPicId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBookByUuid(UUID uuid) {
        bookDao.deleteByUuid(uuid);
    }

    @Override
    public List<Book> findBookByTitle(String keyword) {
        return bookDao.findByTitleLike("%" + keyword + "%");
    }

    @Override
    public Book addBook(Book book) {
        return bookDao.save(book);
    }

}
