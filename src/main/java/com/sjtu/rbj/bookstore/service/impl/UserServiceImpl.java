package com.sjtu.rbj.bookstore.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjtu.rbj.bookstore.dao.UserDao;
import com.sjtu.rbj.bookstore.dto.BookOrderedDTO;
import com.sjtu.rbj.bookstore.dto.PriceHandler;
import com.sjtu.rbj.bookstore.dto.UserInfoDTO;
import com.sjtu.rbj.bookstore.dto.UserStatisticDTO;
import com.sjtu.rbj.bookstore.entity.Book;
import com.sjtu.rbj.bookstore.entity.Order;
import com.sjtu.rbj.bookstore.entity.OrderState;
import com.sjtu.rbj.bookstore.entity.User;
import com.sjtu.rbj.bookstore.entity.User.UserAccount;
import com.sjtu.rbj.bookstore.entity.UserType;
import com.sjtu.rbj.bookstore.service.UserService;

/**
 * @author Bojun Ren
 * @date 2023/04/18
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public Boolean enableLogin(String account, String passwd) {
        Optional<User> maybeUser = userDao.findByAccountAndPasswd(account, passwd);
        return maybeUser.isPresent();
    }

    @Override
    public UserInfoDTO getUserInfoByAccount(String account) {
        Optional<User> maybeUser = userDao.findByAccount(account);
        User user = maybeUser.orElseThrow(() -> new NoSuchElementException("No such account!"));
        List<Order> orderList = user.getOrderList();

        Integer orderId = null;

        for (Order order : orderList) {
            if (OrderState.PENDING == order.getState()) {
                orderId = order.getId();
                break;
            }
        }
        if (orderId == null) {
            Order orderPending = new Order();
            orderPending.setState(OrderState.PENDING);
            user.addOrder(orderPending);
            userDao.flush();
            orderId = orderPending.getId();
        }
        UserInfoDTO userInfo = UserInfoDTO.from(user);
        userInfo.setOrderId(orderId);
        return userInfo;
    }

    @Override
    public Optional<UserType> login(String account, String passwd) {
        Optional<User> maybeUser = userDao.findByAccountAndPasswd(account, passwd);
        if (!maybeUser.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(maybeUser.get().getUserType());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeState(Integer id, UserType state) {
        Optional<User> maybeUser = userDao.findById(id);
        User user = maybeUser.orElseThrow(() -> new NoSuchElementException("No such user!"));
        if (user.getUserType() == UserType.SUPER) {
            throw new UnsupportedOperationException("Can't change the state of a super user!");
        }
        if (user.getUserType() == state) {
            return false;
        }
        user.setUserType(state);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changePasswdByAccount(String account, String newPasswd) {
        Optional<User> maybeUser = userDao.findByAccount(account);
        User user = maybeUser.orElseThrow(() -> new NoSuchElementException("No such account!"));
        return changePasswdByUser(user, newPasswd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changePasswdById(Integer id, String newPasswd) {
        Optional<User> maybeUser = userDao.findById(id);
        User user = maybeUser.orElseThrow(() -> new NoSuchElementException("No such account!"));
        return changePasswdByUser(user, newPasswd);
    }

    private Boolean changePasswdByUser(User user, String newPasswd) {
        UserAccount userAccount = user.getUserAccount();
        /** Reject same passwd */
        if (userAccount.getPasswd().equals(newPasswd)) {
            return false;
        }
        userAccount.setPasswd(newPasswd);
        return true;
    }

    @Override
    public Boolean addUser(String account, String passwd) {
        try {
            userDao.save(new User(account, passwd));
        } catch (DataIntegrityViolationException e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeUserName(Integer id, String newName) {
        Optional<User> maybeUser = userDao.findById(id);
        User user = maybeUser.orElseThrow(() -> new NoSuchElementException("No such user!"));
        user.setName(newName);
    }

    @Override
    public Boolean addUser(String userName, String account, String passwd) {
        User user = new User(account, passwd);
        user.setName(userName);
        try {
            userDao.save(user);
        } catch (DataIntegrityViolationException e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatisticDTO getUserStatistic(Integer userId, Long beginTimestamp,
            Long endTimestamp) {
        Optional<User> maybeUser = userDao.findById(userId);
        User user = maybeUser
                .orElseThrow(() -> new NoSuchElementException("Can't find such user!"));
        List<Order> orderList = user.getOrderList();
        orderList.removeIf(order -> {
            if (order.getState() == OrderState.PENDING) {
                return true;
            }
            long orderTime = order.getTime().getTime();
            return !(orderTime <= endTimestamp && orderTime >= beginTimestamp);
        });
        List<BookOrderedDTO> bookOrderedDTOList = new ArrayList<>();
        Map<Book, Integer> bookOrderedCount = new HashMap<>(16);
        for (var order : orderList) {
            for (var bookOrdered : order.getOrderItemList()) {
                bookOrderedCount.merge(bookOrdered.getBook(), bookOrdered.getQuantity(),
                        (t, u) -> Integer.sum(t, u));
            }
        }
        Integer totalCost = 0;
        for (Map.Entry<Book, Integer> entry : bookOrderedCount.entrySet()) {
            Book book = entry.getKey();
            Integer count = entry.getValue();
            Integer cost = book.getPriceCent() * count;
            bookOrderedDTOList.add(
                    new BookOrderedDTO(book.getUuid(), count, PriceHandler.from(cost).toString()));
            totalCost += cost;
        }

        return new UserStatisticDTO(userId, beginTimestamp, endTimestamp, bookOrderedDTOList,
                PriceHandler.from(totalCost).toString());
    }
}
