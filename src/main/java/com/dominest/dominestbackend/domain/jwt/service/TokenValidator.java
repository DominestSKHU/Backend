package com.dominest.dominestbackend.domain.jwt.service;


import com.dominest.dominestbackend.domain.jwt.constant.AuthScheme;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.auth.JwtAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class TokenValidator {
    public void validateBearer(String authHeader) {
        //  1. 토큰 유무 확인
        if(!StringUtils.hasText(authHeader)){
            throw new JwtAuthException(ErrorCode.NOT_EXISTS_AUTH_HEADER);
        }

        //  2. authorization Bearer 체크
        String[] authorizations = authHeader.split(" ");
        // AuthScheme.BEARER.getType() 은 "Bearer"문자열 반환
        if(authorizations.length < 2 || (!AuthScheme.BEARER.getType().equals(authorizations[0]))){
            throw new JwtAuthException(ErrorCode.NOT_VALID_BEARER_GRANT_TYPE);
        }
    }
}
