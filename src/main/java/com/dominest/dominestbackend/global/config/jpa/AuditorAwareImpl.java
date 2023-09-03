package com.dominest.dominestbackend.global.config.jpa;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        // SecurityContext 에서 Authentication 객체를 꺼내서 email을 꺼내서 리턴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal =
                authentication == null ? "unknown:unknown@un.known"
                : authentication.getPrincipal().toString();

        return Optional.of(principal);
    }
}
