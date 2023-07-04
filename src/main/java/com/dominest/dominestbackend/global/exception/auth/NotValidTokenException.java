package com.dominest.dominestbackend.global.exception.auth;


import com.dominest.dominestbackend.global.exception.BusinessException;
import com.dominest.dominestbackend.global.exception.ErrorCode;

public class NotValidTokenException extends BusinessException {
    public NotValidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}