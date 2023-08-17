package com.dominest.dominestbackend.domain.jwt.service;

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

    // 아래 코드는 토큰타입과 권한이 필요할 경우 doFilter() 로직에 추가
    //            String tokenType = tokenManager.getTokenType(token);
    // 토큰 유형별로 부여할 권한 설정
    // List<GrantedAuthority> authorities;
    // if (tokenType.equalsIgnoreCase("ACCESS")) {
    //     authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    // } else {
    //     authorities = Collections.emptyList();
    // }
}