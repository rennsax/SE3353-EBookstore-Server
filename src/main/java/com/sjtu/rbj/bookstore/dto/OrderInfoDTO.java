package com.sjtu.rbj.bookstore.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.sjtu.rbj.bookstore.constant.Constants;
import com.sjtu.rbj.bookstore.entity.Book;
import com.sjtu.rbj.bookstore.entity.Order;
import com.sjtu.rbj.bookstore.entity.Order.OrderItem;
import com.sjtu.rbj.bookstore.entity.OrderState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bojun Ren
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDTO {

    private Integer id;
    private Integer userId;
    private OrderState state;
    private Timestamp time;
    private String sumBudget;
    private List<BookOrderedDTO> bookOrderedList;

    static public OrderInfoDTO from(Order order) {

        List<OrderItem> orderItemList = order.getOrderItemList();
        List<BookOrderedDTO> bookOrderedList = new ArrayList<>();
        Integer sumBudget = 0;
        for (OrderItem orderItem : orderItemList) {
            Book book = orderItem.getBook();
            Integer totalBudget = orderItem.getQuantity() * book.getPriceCent();
            bookOrderedList.add(BookOrderedDTO.from(orderItem));
            sumBudget += totalBudget;
        }
        return new OrderInfoDTO(order.getId() + Constants.ORDER_NUMBER_BIAS,
                order.getUser().getId(), order.getState(), order.getTime(),
                PriceHandler.from(sumBudget).toString(), bookOrderedList);
    }
}
