package com.dominest.dominestbackend.global.config.security;

import com.dominest.dominestbackend.domain.jwt.constant.AuthScheme;
import com.dominest.dominestbackend.domain.jwt.constant.TokenType;
import com.dominest.dominestbackend.domain.jwt.service.TokenManager;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.auth.JwtAuthException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;

    /** 일반적으로 Exception 발생 시 ExceptionTranslationFilter.rethrow() 하지만,
     * 여기서 발생한 JwtException은 JwtExceptionFilter에서 Catch한다.*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //  1. 토큰 유무 확인
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(authHeader)){
            throw new JwtAuthException(ErrorCode.NOT_EXISTS_AUTH_HEADER);
        }

        //  2. authorization Bearer 체크
        String[] authorizations = authHeader.split(" ");
        // AuthScheme.BEARER.getType() 은 "Bearer"문자열 반환
        if(authorizations.length < 2 || (!AuthScheme.BEARER.getType().equals(authorizations[0]))){
            throw new JwtAuthException(ErrorCode.NOT_VALID_BEARER_GRANT_TYPE);
        }

        String token = authorizations[1]; // Bearer 뒤의 토큰 몸통 부분
        // 3. 토큰 유효성(변조) 검사
        if (! tokenManager.validateToken(token)) {
            throw new JwtAuthException(ErrorCode.NOT_VALID_TOKEN);
        }

        //  4. 토큰 타입 검증
        String tokenType = tokenManager.getTokenType(token);
        if(!TokenType.ACCESS.name().equals(tokenType)) { // ACCESS 토큰이 아니면
            throw new JwtAuthException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        Claims claims = tokenManager.getTokenClaims(token);
        // 5. 토큰 만료 검사
        if (tokenManager.isTokenExpired(claims.getExpiration())) {
            throw new JwtAuthException(ErrorCode.TOKEN_EXPIRED);
        }

        String email = claims.getAudience();
        // 마지막으로 인증 정보로 Audience  저장
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}