package com.sjtu.rbj.bookstore.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sjtu.rbj.bookstore.entity.User;

/**
 * @author Bojun Ren
 * @date 2023/04/17
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Retrieves a user entity by its account (defined as unique key).
     *
     * @param account must not be {@literal null}.
     * @return the entity with the given account or {@literal Optional#empty()} if none found.
	 * @throws IllegalArgumentException if {@literal account} is {@literal null}.
     */
    Optional<User> findByUserAccountAccount(String account);

    /**
     * Retrieves a user entity by its account (defined as unique key) and passwd.
     *
     * @param account must not be {@literal null}.
     * @param passwd must not be {@literal null}.
     * @return the entity with the given account or {@literal Optional#empty()} if none found.
	 * @throws IllegalArgumentException if either {@literal account} or {@literal passwd} is {@literal null}.
     */
    Optional<User> findByUserAccountAccountAndUserAccountPasswd(String account, String passwd);
}
