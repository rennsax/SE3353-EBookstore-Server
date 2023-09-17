package com.sjtu.rbj.bookstore.dto;

import java.sql.Date;
import java.util.UUID;

import com.sjtu.rbj.bookstore.entity.Book;

import lombok.Getter;
import lombok.ToString;

/**
 * In fact, this DTO just transfer the price field into String.
 *
 * @author Bojun Ren
 */
@Getter
@ToString
public class BookDTO {
    private UUID uuid;
    private String title;
    private String picId;
    private String price;
    private String author;
    private Date date;
    private String isbn;
    private Integer stock;
    private String description;

    /**
     * Package the book entity into book data.
     *
     * @param book
     * @return book data which can be returned to front-end.
     */
    public static BookDTO from(Book book) {
        return new BookDTO(book.getUuid(), book.getTitle(), book.getPicId(),
                PriceHandler.from(book.getPriceCent()).toString(), book.getAuthor(), book.getDate(),
                book.getIsbn(), book.getDescription(), book.getStock());
    }

    public BookDTO(UUID uuid, String title, String picId, String price, String author, Date date,
            String isbn, String description, Integer stock) {
        this.uuid = uuid;
        this.title = title;
        this.picId = picId;
        this.price = price;
        this.author = author;
        this.date = date;
        this.isbn = isbn;
        this.description = description;
        this.stock = stock;
    }
}
