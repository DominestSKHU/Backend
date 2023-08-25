package com.dominest.dominestbackend.global.config.security;

import com.dominest.dominestbackend.global.exception.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// 403 에러 핸들러를 구현.
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Custom error response.
        ErrorResponseDto<String> errDto = new ErrorResponseDto<>(HttpStatus.FORBIDDEN.value()
                , HttpStatus.FORBIDDEN
                , "인가되지 않은 사용자입니다.");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errDto);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }
}
