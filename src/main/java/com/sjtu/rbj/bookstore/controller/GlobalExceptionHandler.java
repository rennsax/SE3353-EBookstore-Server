package com.sjtu.rbj.bookstore.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sjtu.rbj.bookstore.dto.ApiErrorResponse;
import com.sjtu.rbj.bookstore.exception.IncompleteRequestBodyException;

/**
 * @author Bojun Ren
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler({ HttpMessageNotReadableException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<?> handleHttpMessageNotReadableException(Exception e) {
        return ApiErrorResponse.builder().errorCode("A0400")
                .errorMessage(
                        "Unreadable http request (possibly because of wrong response body format)")
                .build();
    }

    @ExceptionHandler({ IncompleteRequestBodyException.class })
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ApiErrorResponse<?> handleIncompleteRequestBodyException(
            IncompleteRequestBodyException e) {
        return ApiErrorResponse.builder().errorCode("A0400").errorMessage(e.getMessage()).build();
    }

    @ExceptionHandler({ UnsupportedOperationException.class, NoSuchElementException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse<?> handleOperationException(Exception e) {
        return ApiErrorResponse.builder().errorCode("A0500").errorMessage(e.getMessage()).build();
    }

}
