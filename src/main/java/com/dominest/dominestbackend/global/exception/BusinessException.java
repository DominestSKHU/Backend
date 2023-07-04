package com.dominest.dominestbackend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private final int statusCode;
    private final HttpStatus httpStatus;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // throwable 의 detailMessage 에 들어가며, throwable.getMessage()로 부를 수 있다.
        this.statusCode = errorCode.getStatus();
        this.httpStatus = HttpStatus.valueOf(errorCode.getStatus());
    }
}
