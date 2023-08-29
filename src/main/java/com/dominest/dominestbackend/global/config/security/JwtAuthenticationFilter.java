package com.dominest.dominestbackend.global.config.security;

import com.dominest.dominestbackend.domain.jwt.constant.TokenType;
import com.dominest.dominestbackend.domain.jwt.service.TokenManager;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 유효성을 검사한다. 메서드 내부에서 예외상황시 바로 return으로 빠져나오고 다음 필터를 동작시킨다.
        filter(request);

        filterChain.doFilter(request, response);
    }

    private void filter(HttpServletRequest request){
        String token = resolveToken(request);
        // 1. 토큰이 존재하고 유효한 경우에만 토큰 파싱
        if (! StringUtils.hasText(token)) {
            return;
        }

        // 2. 토큰 유효성(변조) 검사
        if (! tokenManager.validateToken(token)) {
            return;
        }

        Claims claims = tokenManager.getTokenClaims(token);
        // 3. 토큰 만료 검사
        if (tokenManager.isTokenExpired(claims.getExpiration())) {
            return;
        }

        //  4. 토큰 타입 검증
        String tokenType = tokenManager.getTokenType(token);
        if(!TokenType.ACCESS.name().equals(tokenType)) { // ACCESS 토큰이 아니면
            return;
        }

        String email = claims.getAudience();
        // 마지막으로 인증 정보로 Audience  저장
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
   }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return "";
    }

}