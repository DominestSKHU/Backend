package com.dominest.dominestbackend.global.exception.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponseDto<T> {
    private int statusCode;
    private String httpStatus;
    private T errorMessage;

    public ErrorResponseDto(int statusCode, HttpStatus httpStatus, T errorMessage) {
        this.statusCode = statusCode;
        this.httpStatus = httpStatus.getReasonPhrase();
        this.errorMessage = errorMessage;
    }
}
