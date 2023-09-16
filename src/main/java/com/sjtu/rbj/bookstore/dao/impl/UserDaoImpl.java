package com.sjtu.rbj.bookstore.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sjtu.rbj.bookstore.dao.UserDao;
import com.sjtu.rbj.bookstore.entity.User;
import com.sjtu.rbj.bookstore.repository.UserRepository;

/**
 * @author Bojun Ren
 * @date 2023/04/18
 *
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserRepository repository;

    @Override
    public Optional<User> findByAccount(String account) {
        return repository.findByUserAccountAccount(account);
    }

    @Override
    public Optional<User> findByAccountAndPasswd(String account, String passwd) {
        return repository.findByUserAccountAccountAndUserAccountPasswd(account, passwd);
    }

    @Override
    public void flush() {
        repository.flush();
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public <S extends User> S save(S entity) {
        return repository.save(entity);
    }

}
