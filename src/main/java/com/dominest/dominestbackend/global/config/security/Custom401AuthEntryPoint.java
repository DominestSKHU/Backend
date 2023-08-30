package com.dominest.dominestbackend.global.config.security;

import com.dominest.dominestbackend.global.exception.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 401 에러 핸들러를 구현.
@Component
public class Custom401AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Custom error response.
        ErrorResponseDto<String> errDto = new ErrorResponseDto<>(HttpStatus.UNAUTHORIZED.value()
                , HttpStatus.UNAUTHORIZED
                , "인증되지 않은 사용자입니다.");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), errDto);
    }
}
