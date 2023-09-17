package com.sjtu.rbj.bookstore.dao.impl;

import java.util.List;
import java.util.Optional;

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
    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByAccount(String account) {
        return userRepository.findByUserAccountAccount(account);
    }

    @Override
    public Optional<User> findByAccountAndPasswd(String account, String passwd) {
        return userRepository.findByUserAccountAccountAndUserAccountPasswd(account, passwd);
    }

    @Override
    public void flush() {
        userRepository.flush();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public <S extends User> S save(S entity) {
        return userRepository.save(entity);
    }

    private final UserRepository userRepository;
}
