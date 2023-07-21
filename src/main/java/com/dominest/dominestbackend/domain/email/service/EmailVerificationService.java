package com.dominest.dominestbackend.domain.email.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationService {
    @Getter
    private final Cache<String, String> codeExpirationCache; // <email, verification code>. thread-safe map.
    private final Cache<String, Boolean> emailVerificationStatusCache; // <email, verification status>. thread-safe map.
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public EmailVerificationService() {
        codeExpirationCache = Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES)
                .build();

        emailVerificationStatusCache = Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES)
                .build();
    }

    public String generateCode(String email) {
        // 4자리 검증 코드 생성 후 캐시에 저장
        String verificationCode = UUID.randomUUID().toString().substring(0, 4);
        codeExpirationCache.put(email, verificationCode);

        // 20분 후에 캐시에서 삭제. 검증코드는 20분간 유효
        executorService.schedule(() -> invalidateCode(email), 20, TimeUnit.MINUTES);
        return verificationCode;
    }

    // fixme: expireAfterWrite 와 기능이 겹치는지 확인
    private void invalidateCode(String email) {
        // Discards any cached value for the key.
        codeExpirationCache.invalidate(email);
    }

    public boolean verifyCode(String email, String verificationCode) {
        if (email == null || verificationCode == null) {
            return false;
        }

        String storedCode = codeExpirationCache.getIfPresent(email);
        boolean isValid = storedCode != null && storedCode.equalsIgnoreCase(verificationCode);

        // Cache the email verification status
        if (isValid) {
            emailVerificationStatusCache.put(email, true);
        }

        return isValid;
    }

    public boolean isEmailVerified(String email) {
        return emailVerificationStatusCache.getIfPresent(email) != null;
    }
}