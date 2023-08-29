package com.dominest.dominestbackend.global.exception.exceptions.auth;

import com.dominest.dominestbackend.global.exception.ErrorCode;

public class JwtException extends AuthException {
    public JwtException(ErrorCode errorCode) {
        super(errorCode);
    }
}
