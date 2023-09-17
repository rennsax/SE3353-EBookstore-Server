package com.sjtu.rbj.bookstore.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sjtu.rbj.bookstore.annotation.Administer;
import com.sjtu.rbj.bookstore.constant.Constants;
import com.sjtu.rbj.bookstore.dto.BookOrdered;
import com.sjtu.rbj.bookstore.dto.OrderInfoDTO;
import com.sjtu.rbj.bookstore.entity.Order;
import com.sjtu.rbj.bookstore.exception.IncompleteRequestBodyException;
import com.sjtu.rbj.bookstore.service.OrderService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Bojun Ren
 */
@RestController
@RequestMapping(path = "/orders")
@CrossOrigin(Constants.ALLOW_ORIGIN)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public OrderInfoDTO getOrderInfo(@PathVariable Integer orderId) {
        return orderService.getOrderInfoByOrderId(orderId);
    }

    /**
     * Handle the request to update an order, including add or delete some ordered
     * books for the order. The request body should contains {@code uuid} and
     * {@code quantity}.
     */
    @PatchMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrder(@PathVariable Integer orderId, @RequestBody OrderRequestBody body) {

        if (OrderRequestBody.OP_UPDATE.equals(body.getOp())) {
            // OP: update items
            BookOrdered bookOrdered = body.getBookOrdered();

            if (bookOrdered == null) {
                throw new IncompleteRequestBodyException("Please provide bookOrdered info");
            }
            // Check if the request body is complete
            if (bookOrdered.getUuid() == null || bookOrdered.getQuantity() == null) {
                throw new IncompleteRequestBodyException("Incomplete bookOrdered info");
            }

            Boolean successUpdate = orderService.updateOrder(orderId, bookOrdered.getUuid(),
                    bookOrdered.getQuantity());
            if (!successUpdate) {
                throw new UnsupportedOperationException("Update items failed!");
            }

        } else if (OrderRequestBody.OP_CHECKOUT.equals(body.getOp())) {
            // OP: checkout (ignore other fields)
            orderService.submitOrder(orderId);

        } else {
            // OP: any other op (invalid)
            throw new IncompleteRequestBodyException("Unknown operation");
        }
    }

    @GetMapping
    public List<OrderInfoDTO> getAllOrderByUserId(@RequestParam("userId") Integer userId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "beginDate", required = false) String beginDate,
            @RequestParam(name = "endDate", required = false) String endDate)
            throws ParseException {
        List<Order> orderList = null;
        if (keyword == null) {
            orderList = orderService.getOrderByUserId(userId);
        } else {
            orderList = orderService.getOrderByUserId(userId, keyword);
        }
        filterByBeginAndEnd(orderList, beginDate, endDate);
        List<OrderInfoDTO> orderInfoDTOList = new ArrayList<>();
        orderList.forEach(order -> orderInfoDTOList.add(OrderInfoDTO.from(order)));
        return orderInfoDTOList;
    }

    @Administer
    @GetMapping("/all")
    public List<OrderInfoDTO> getAllOrders(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "beginDate", required = false) String beginDate,
            @RequestParam(name = "endDate", required = false) String endDate)
            throws ParseException {
        List<Order> orderList = null;
        if (keyword == null) {
            orderList = orderService.getAllOrders();
        } else {
            orderList = orderService.getAllOrders(keyword);
        }
        filterByBeginAndEnd(orderList, beginDate, endDate);
        List<OrderInfoDTO> orderInfoDTOList = new ArrayList<>();
        orderList.forEach(order -> orderInfoDTOList.add(OrderInfoDTO.from(order)));
        return orderInfoDTOList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static public class OrderRequestBody {
        private String op;
        private BookOrdered bookOrdered;

        public static final String OP_UPDATE = "update items";
        public static final String OP_CHECKOUT = "checkout";
    }

    private void filterByBeginAndEnd(List<Order> orderList, String beginDate, String endDate)
            throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (beginDate != null) {
            Date date = parser.parse(beginDate + " 00:00:00");
            orderList.removeIf(order -> date.getTime() > order.getTime().getTime());
        }
        if (endDate != null) {
            Date date = parser.parse(endDate + " 23:59:59");
            orderList.removeIf(order -> date.getTime() < order.getTime().getTime());
        }
    }
}
