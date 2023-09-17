package com.sjtu.rbj.bookstore.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Bojun Ren
 * @date 2023/04/08
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "`user`")
@Check(constraints = "`user_type` in ('NORMAL', 'SUPER', 'FORBIDDEN')")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType = UserType.NORMAL;

    private String name = "Default User";

    @Column(name = "avatar_id", columnDefinition = "CHAR(15)", nullable = false)
    @ColumnDefault("'default_user'")
    private String avatarId = "default_user";

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "account_id", unique = true, nullable = false)
    @Embedded
    private UserAccount userAccount = new UserAccount();

    /**
     * Create an user entity with the account and password,
     * leave the primary key null, with default user type {@literal normal}
     *
     * @param account
     * @param passwd
     */
    public User(String account, String passwd) {
        this.userAccount.setAccount(account);
        this.userAccount.setPasswd(passwd);
    }

    @Transient
    public void setPasswd(String passwd) {
        this.userAccount.setPasswd(passwd);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @RequiredArgsConstructor
    @ToString
    @Table(name = "`user_account`")
    @Entity
    @Embeddable
    public static class UserAccount {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NonNull
        @Column(nullable = false, unique = true)
        private String account;

        @NonNull
        @Column(nullable = false)
        private String passwd;

        @ToString.Exclude
        @OneToOne(mappedBy = "userAccount")
        @JoinColumn(name = "user_id") // useless
        private User user;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("`time` desc")
    private List<Order> orderList = new ArrayList<>();

    public Boolean addOrder(Order order) {
        Boolean isAdded = orderList.add(order);
        order.setUser(this);
        return isAdded;
    }

    public Boolean removeOrder(Order order) {
        Boolean isRemoved = this.orderList.remove(order);
        if (isRemoved) {
            order.setUser(null);
        }
        return isRemoved;
    }

    public Order removeOrder(int index) {
        return this.orderList.remove(index);
    }

    public void clearOrder() {
        this.orderList.clear();
    }

}