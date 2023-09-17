package com.sjtu.rbj.bookstore.dto;

import java.util.UUID;

import com.sjtu.rbj.bookstore.entity.Book;
import com.sjtu.rbj.bookstore.entity.Order.OrderItem;

import lombok.Getter;

/**
 * @author Bojun Ren
 */
@Getter
public class BookOrderedDTO extends BookOrdered {

    private String totalBudget;


    public BookOrderedDTO(UUID uuid, Integer quantity, String totalBudget) {
        super(uuid, quantity);
        this.totalBudget = totalBudget;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(super.toString());
        buffer.append(", totalBudget='");
        buffer.append(getTotalBudget());
        buffer.append("'");
        return buffer.toString();
    }

    static public BookOrderedDTO from(OrderItem orderItem) {
        Book book = orderItem.getBook();
        Integer quantity = orderItem.getQuantity();
        return new BookOrderedDTO(book.getUuid(), quantity,
                PriceHandler.from(quantity * book.getPriceCent()).toString());
    }
}
