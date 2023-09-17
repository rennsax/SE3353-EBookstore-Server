package com.sjtu.rbj.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sjtu.rbj.bookstore.constant.Constants;
import com.sjtu.rbj.bookstore.service.FileStorageService;

/**
 * @author Bojun Ren
 */
@RestController
@CrossOrigin(value = Constants.ALLOW_ORIGIN)
public class StorageController {

    @Autowired
    FileStorageService storageService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void receiveFile(@RequestPart("file") MultipartFile file) {
        storageService.save(file);
    }

}
