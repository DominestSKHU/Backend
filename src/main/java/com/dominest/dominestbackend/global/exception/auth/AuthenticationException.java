package com.dominest.dominestbackend.global.exception.auth;


import com.dominest.dominestbackend.global.exception.BusinessException;
import com.dominest.dominestbackend.global.exception.ErrorCode;

public class AuthenticationException extends BusinessException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
