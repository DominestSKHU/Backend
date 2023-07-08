package com.dominest.dominestbackend.api.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;
// 응답 템플릿
// 응답 DTO를 리스트로 보낼 때
@Getter
public class RspsTemplate<T> {
    private int statusCode;
    private List<T> data;

    public RspsTemplate(HttpStatus httpStatus, List<T> data) {
        this.statusCode = httpStatus.value();
        this.data = data;
    }
}
