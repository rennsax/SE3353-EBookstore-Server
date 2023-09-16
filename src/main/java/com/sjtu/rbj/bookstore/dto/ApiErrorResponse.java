package com.sjtu.rbj.bookstore.dto;

import java.io.Serializable;

import org.springframework.lang.Nullable;

import lombok.Builder;
import lombok.Data;

/**
 * @author Bojun Ren
 * @data 2023/05/31
 */
@Data
@Builder
public class ApiErrorResponse<T> implements Serializable {
    private String errorCode;
    private String errorMessage;

    @Nullable
    private T data;
}
