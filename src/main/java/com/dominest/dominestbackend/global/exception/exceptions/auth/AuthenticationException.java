package com.dominest.dominestbackend.global.exception.exceptions.auth;


import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;

public class AuthenticationException extends AppServiceException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
