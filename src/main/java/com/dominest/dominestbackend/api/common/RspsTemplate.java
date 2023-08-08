package com.dominest.dominestbackend.api.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 성응템
@Getter
public class RspsTemplate<T> {
    private int statusCode;
    private T data;

    private String message;

    public RspsTemplate(HttpStatus httpStatus, T data, String message) {
        this.statusCode = httpStatus.value();
        this.data = data;
        this.message = message;
    }
}
