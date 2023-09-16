package com.sjtu.rbj.bookstore.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sjtu.rbj.bookstore.dao.BookDao;
import com.sjtu.rbj.bookstore.entity.Book;
import com.sjtu.rbj.bookstore.repository.BookRepository;

/**
 * @author Bojun Ren
 * @date 2023/04/19
 */
@Repository
public class BookDaoImpl implements BookDao {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public Optional<Book> findByUuid(UUID uuid) {
        return bookRepository.findByUuid(uuid);
    }

    @Override
    public List<Book> findWithLimitWithOffset(Integer limit, Integer offset) {
        return bookRepository.findWithLimitWithOffset(limit, offset);
    }

    @Override
    public Optional<Book> findById(Integer id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        bookRepository.deleteByUuid(uuid);
    }

    @Override
    public List<Book> findByTitleLike(String like) {
        return bookRepository.findByTitleLike(like);
    }

    @Override
    public <S extends Book> S save(S entity) {
        return bookRepository.save(entity);
    }

}
