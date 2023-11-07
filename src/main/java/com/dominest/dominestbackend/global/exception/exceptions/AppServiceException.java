package com.dominest.dominestbackend.global.exception.exceptions;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 비즈니스 로직이 아닌 애플리케이션 서비스 로직상 예외
@Getter
public class AppServiceException extends RuntimeException{
    private final int statusCode;
    private final HttpStatus httpStatus;

    public AppServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.statusCode = errorCode.getStatusCode();
        this.httpStatus = HttpStatus.valueOf(errorCode.getStatusCode());
    }

    public AppServiceException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.statusCode = errorCode.getStatusCode();
        this.httpStatus = HttpStatus.valueOf(errorCode.getStatusCode());
    }
}
