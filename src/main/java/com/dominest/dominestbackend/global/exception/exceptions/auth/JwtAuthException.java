package com.dominest.dominestbackend.global.exception.exceptions.auth;

import com.dominest.dominestbackend.global.exception.ErrorCode;

public class JwtAuthException extends AuthException {
    public JwtAuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
