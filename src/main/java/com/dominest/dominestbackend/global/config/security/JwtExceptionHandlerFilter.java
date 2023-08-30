package com.dominest.dominestbackend.global.config.security;

import com.dominest.dominestbackend.global.exception.dto.ErrorResponseDto;
import com.dominest.dominestbackend.global.exception.exceptions.auth.JwtAuthException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
//토큰 관련 예외 처리
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            filterChain.doFilter(request, response);
        } catch (JwtAuthException e){
            // Custom error response.
            ErrorResponseDto<String> errDto = new ErrorResponseDto<>(HttpStatus.UNAUTHORIZED.value()
                    , HttpStatus.UNAUTHORIZED
                    , e.getMessage());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            new ObjectMapper().writeValue(response.getWriter(), errDto);
        }
    }
}
