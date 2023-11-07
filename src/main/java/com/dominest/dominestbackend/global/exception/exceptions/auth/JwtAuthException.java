package com.dominest.dominestbackend.global.exception.exceptions.auth;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;

public class JwtAuthException extends AppServiceException {
    public JwtAuthException(ErrorCode errorCode) {
        super(errorCode);
    }
    public JwtAuthException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
