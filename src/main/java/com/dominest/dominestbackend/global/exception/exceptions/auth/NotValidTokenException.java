package com.dominest.dominestbackend.global.exception.exceptions.auth;


import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;

public class NotValidTokenException extends AppServiceException {
    public NotValidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}