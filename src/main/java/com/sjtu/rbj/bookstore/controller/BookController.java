package com.sjtu.rbj.bookstore.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sjtu.rbj.bookstore.annotation.Administer;
import com.sjtu.rbj.bookstore.constant.Constants;
import com.sjtu.rbj.bookstore.dto.ApiErrorResponse;
import com.sjtu.rbj.bookstore.dto.BookDTO;
import com.sjtu.rbj.bookstore.entity.Book;
import com.sjtu.rbj.bookstore.service.BookService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Bojun Ren
 */
@Slf4j
@RestController
@RequestMapping("/books")
@CrossOrigin(Constants.ALLOW_ORIGIN)
public class BookController {

    @Administer
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addBook(@RequestBody Book book) {
        bookService.addBook(book);
    }

    @GetMapping
    public List<BookDTO> getBookListForHomePage(@RequestParam(defaultValue = "4") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(value = "s", required = false) String keyword) {

        List<Book> bookList;
        if (keyword == null) {
            bookList = bookService.getBookDataListForHomePage(limit, offset);
        } else {
            bookList = bookService.findBookByTitle(keyword);
        }
        List<BookDTO> bookDataList = new ArrayList<>();
        for (Book book : bookList) {
            bookDataList.add(BookDTO.from(book));
        }
        return bookDataList;
    }

    @PatchMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(@RequestBody BookDTO bookDTO) {
        bookService.updateBook(bookDTO);
    }

    @Administer
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable UUID uuid) {
        bookService.deleteBookByUuid(uuid);
    }

    @Administer
    @GetMapping("/all")
    public List<BookDTO> getAllBooks() {
        List<Book> allBooks = bookService.getAllBooks();
        List<BookDTO> bookDataList = new ArrayList<>();
        for (Book book : allBooks) {
            bookDataList.add(BookDTO.from(book));
        }
        log.info("Return {} books.", bookDataList.size());
        return bookDataList;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getBookByUuid(@PathVariable("uuid") UUID uuid) {
        try {
            Book book = bookService.getBookByUuid(uuid);
            return ResponseEntity.ok().body(BookDTO.from(book));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiErrorResponse.builder()
                    .errorCode("B0320").errorMessage(ex.getMessage()).build());
        }
    }

    @Autowired
    private BookService bookService;

}
