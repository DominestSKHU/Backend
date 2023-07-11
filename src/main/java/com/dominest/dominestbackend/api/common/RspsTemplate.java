package com.dominest.dominestbackend.api.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 응답 템플릿
@Getter
public class RspsTemplate<T> {
    private int statusCode;
    private T data;

    public RspsTemplate(HttpStatus httpStatus, T data) {
        this.statusCode = httpStatus.value();
        this.data = data;
    }
}
