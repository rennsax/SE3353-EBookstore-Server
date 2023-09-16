package com.sjtu.rbj.bookstore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sjtu.rbj.bookstore.service.FileStorageService;

import jakarta.annotation.Resource;

/**
 * @author Bojun Ren
 * @date 2023/04/08
 */
@SpringBootApplication
public class BookstoreApplication implements CommandLineRunner {

    @Resource
    FileStorageService fileStorageService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        fileStorageService.init();
    }
}
