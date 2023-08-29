package com.dominest.dominestbackend.global.exception.exceptions.auth;


import com.dominest.dominestbackend.global.exception.ErrorCode;

public class NotValidTokenException extends AuthException {
    public NotValidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}