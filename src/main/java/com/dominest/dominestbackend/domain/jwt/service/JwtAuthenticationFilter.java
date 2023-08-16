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
        // 토큰 정보 가져오기
        String token = resolveToken(request);

        if (StringUtils.hasText(token) && tokenManager.validateToken(token)) { // 토큰이 존재하고 유효한 경우
            Claims claims = tokenManager.getTokenClaims(token);

            String email = tokenManager.getMemberEmail(token);
//                String tokenType = tokenManager.getTokenType(token);

            // 토큰 유형별로 부여할 권한 설정
            // List<GrantedAuthority> authorities;
            // if (tokenType.equalsIgnoreCase("ACCESS")) {
            //     authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            // } else {
            //     authorities = Collections.emptyList();
            // }

            if (! tokenManager.isTokenExpired(claims.getExpiration())) {
                // 인증 정보 저장
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터로 전달
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return "";
    }
}