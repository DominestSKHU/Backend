package com.dominest.dominestbackend.global.exception.exceptions;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 비즈니스 로직 상 예외
@Getter
public class BusinessException extends RuntimeException {
    private final int statusCode;
    private final HttpStatus httpStatus;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // throwable 의 detailMessage 에 들어가며, throwable.getMessage()로 부를 수 있다.
        this.statusCode = errorCode.getStatusCode();
        this.httpStatus = HttpStatus.valueOf(errorCode.getStatusCode());
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message); // throwable 의 detailMessage 에 들어가며, throwable.getMessage()로 부를 수 있다.
        this.statusCode = httpStatus.value();
        this.httpStatus = httpStatus;
    }
}