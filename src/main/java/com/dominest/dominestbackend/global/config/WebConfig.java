package com.dominest.dominestbackend.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해
                .allowedOriginPatterns("*")    // 모든 오리진 허용
                .allowedMethods("*")    // 모든 메소드 허용
                .allowedHeaders("*")    // 모든 헤더 허용
                .maxAge(3600L);         // preflight 캐시
    }

}










