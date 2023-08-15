package com.dominest.dominestbackend.global.config.jpa;

import com.dominest.dominestbackend.domain.jwt.service.TokenManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final HttpServletRequest httpServletRequest;
    private final TokenManager tokenManager;

    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        // SecurityContext 에서 Authentication 객체를 꺼내서 email을 꺼내서 리턴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal =
                authentication == null ? "unknown"
                : (String) authentication.getPrincipal();

//        //  1. authorization 필수 체크. 헤더 부분에 Authorization 이 없으면 지정한 예외를 발생시킴
//        //  토큰 유무 확인
//        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
//
//        String email = tokenManager.getMemberEmail(token);
        return Optional.of(principal);
    }
}
