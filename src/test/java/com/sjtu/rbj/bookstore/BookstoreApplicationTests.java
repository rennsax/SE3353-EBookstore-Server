package com.sjtu.rbj.bookstore;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.sjtu.rbj.bookstore.controller.LoginController;
import com.sjtu.rbj.bookstore.controller.UserController;
import com.sjtu.rbj.bookstore.dao.BookDao;
import com.sjtu.rbj.bookstore.dao.OrderDao;
import com.sjtu.rbj.bookstore.dao.UserDao;
import com.sjtu.rbj.bookstore.dto.OrderInfoDTO;
import com.sjtu.rbj.bookstore.dto.PriceHandler;
import com.sjtu.rbj.bookstore.entity.Book;
import com.sjtu.rbj.bookstore.entity.Order;
import com.sjtu.rbj.bookstore.entity.Order.OrderItem;
import com.sjtu.rbj.bookstore.entity.OrderState;
import com.sjtu.rbj.bookstore.entity.User;
import com.sjtu.rbj.bookstore.entity.UserType;
import com.sjtu.rbj.bookstore.repository.BookRepository;
import com.sjtu.rbj.bookstore.repository.OrderRepository;
import com.sjtu.rbj.bookstore.repository.UserRepository;
import com.sjtu.rbj.bookstore.service.OrderService;
import com.sjtu.rbj.bookstore.service.UserService;

@SpringBootTest
class BookstoreApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserDao userDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    BookDao bookDao;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    DataSource dataSource;

    @Test
    @Transactional
    @Rollback(true)
    public void testUserService() {
        /** Can't ban the super user. */
        assertThrows(UnsupportedOperationException.class, () -> {
            userService.changeState(1, UserType.FORBIDDEN);
        });
        assertDoesNotThrow(() -> {
            userService.changeState(2, UserType.FORBIDDEN);
        });
        /** Can't ban multiple times. */
        assertFalse(userService.changeState(2, UserType.FORBIDDEN));

        assertTrue(userService.changePasswdByAccount("cauchy@gmail.com", "hello"));
        assertFalse(userService.login("cauchy@gmail.com", "123456").isPresent());
        assertTrue(userService.login("cauchy@gmail.com", "hello").isPresent());
        assertFalse(userService.changePasswdByAccount("bob@outlook.com", "abcdef"));
        assertTrue(userService.addUser("test@163.com", "test123"));
        assertDoesNotThrow(() -> {
            userService.changeUserName(3, "Juliet");
        });
        userDao.flush();
        assertEquals("Juliet", userService.getAllUsers().get(2).getName());
        assertFalse(userService.addUser("test@163.com", "test123"));
    }

    /**
     * test whether the entities can be correctly performed
     */
    @Test
    @Transactional(readOnly = true)
    void testEntities() {
        /** `book` table (initial data from "data-mysql.sql") */
        Long bookCount = bookRepository.count();
        assertEquals(46, bookCount, "books count error!");

        /** `user` table */
        Optional<User> maybeUser = userRepository
                .findByUserAccountAccountAndUserAccountPasswd("cauchy@gmail.com", "123456");
        assertTrue(maybeUser.isPresent(), "user \"cauchy\" not found!");
        User user = maybeUser.get();
        user.setName("cauchy");
        assertEquals(4, user.getOrderList().size());

        /** `order` table */
        List<Order> orderAll = orderRepository.findAll();
        assertEquals(4, orderAll.size());
        Order order = orderAll.get(1); /** the second order, PENDING, contains two items */
        assertEquals(user, order.getUser());
    }

    @Test
    @Transactional(rollbackFor = { Exception.class })
    @Rollback(true)
    void testOrderService() {
        /** Test method: getOrderInfoByOrderId */
        OrderInfoDTO orderInfo = orderService.getOrderInfoByOrderId(1);
        assertEquals(3, orderInfo.getBookOrderedList().size());

        /** Test method: updateOrder */
        Book book = bookRepository.findById(1).get();
        UUID uuid = book.getUuid();
        System.out.println(uuid);
        orderService.updateOrder(4, uuid, 100);
        Order order4 = orderDao.findById(4).get();
        assertEquals(4, order4.getOrderItemList().size());
        List<OrderItem> orderItemList = order4.getOrderItemList();
        uuid = null;
        for (OrderItem orderItem : orderItemList) {
            if (orderItem.getQuantity().equals(1)) {
                uuid = orderItem.getBook().getUuid();
                break;
            }
        }
        assertNotNull(uuid);
        orderService.updateOrder(4, uuid, -1);
        assertEquals(3, order4.getOrderItemList().size());

        /** Test method: submitOrder */
        orderService.submitOrder(4);
        assertEquals(OrderState.TRANSPORTING, orderDao.findById(2).get().getState());

        assertThrows(NoSuchElementException.class, () -> orderService.getOrderInfoByOrderId(5));
        assertThrows(UnsupportedOperationException.class, () -> orderService.submitOrder(1));
    }

    // @BeforeEach
    @Test
    @Transactional
    @Rollback(true)
    void initializeDatabase() {
        /** before this initialization, data-mysql.sql has been loaded */
        /** BEGIN insert book */
        Book book = new Book();
        book.setTitle("~test book~");
        assertNull(book.getUuid());
        bookRepository.save(book);
        assertNotNull(book.getUuid());
        /** END insert book */

        /** insert some relations */
        /** insert a user */
        User user = new User("cauchy", "123");
        userRepository.save(user);

        /** insert two orders */
        Order order1 = new Order();
        order1.setUser(user);
        Order order2 = new Order();
        order2.setState(OrderState.PENDING);
        order2.setUser(user);

        orderRepository.save(order1);
        assertEquals(5, orderRepository.count());
        orderRepository.save(order2);

        /** find two books for order items of order2 */
        Optional<Book> book1 = bookRepository.findById(10);
        Optional<Book> book2 = bookRepository.findById(20);
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setBook(book1.get());
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setBook(book2.get());
        orderItem1.setQuantity(1);
        orderItem2.setQuantity(2);

        /** insert two order items for order2 */
        order2.addOrderItem(orderItem1);
        order2.addOrderItem(orderItem2);

        /** find one books for order items of order1 */
        book1 = bookRepository.findById(15);
        orderItem1 = new OrderItem();
        orderItem1.setQuantity(3);
        orderItem1.setBook(book1.get());

        /** insert two order items for order1 */
        order1.addOrderItem(orderItem1);
    }

    @Test
    void testPrice() {
        Book book = bookRepository.findById(2).get();
        assertEquals(4000, book.getPriceCent());

        assertEquals(4000, bookRepository.findAll().get(1).getPriceCent());

        assertEquals(4000, bookRepository.findWithLimitWithOffset(1, 1).get(0).getPriceCent());
    }

    @Test
    void testPriceHandler() {
        Integer price1 = 1;
        assertEquals("0.01", PriceHandler.from(price1).toString());
        assertEquals("0.001", PriceHandler.of(price1, 3).toString());
        Integer price2 = 22;
        assertEquals("0.22", PriceHandler.from(price2).toString());
        assertEquals("2.2", PriceHandler.of(price2, 1).toString());
    }

    @Autowired
    UserController userController;

    @Autowired
    LoginController loginController;

    @Test
    void testLoginController() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("account", "root");
        requestBody.put("passwd", "root123");
        ResponseEntity<?> response = loginController.checkLogin(requestBody);
        Map<String, String> expectBody = new HashMap<>(1);
        expectBody.put("userType", "SUPER");
        assertEquals(expectBody, response.getBody());
    }

    @Test
    void testBookSearch() {
        List<Book> bookList = bookRepository.findByTitleLike("%python%");
        assertEquals(5, bookList.size());
    }

}