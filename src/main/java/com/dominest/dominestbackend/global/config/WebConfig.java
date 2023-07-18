package com.dominest.dominestbackend.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // private final AuthenticationInterceptor authenticationInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authenticationInterceptor)
//                .order(1)   // 인증 인터셉터를 첫 번째로 수행
//                .addPathPatterns("/**")     // 이 경로를 대상으로 동작
//                .excludePathPatterns("/", "/admin/login", "/admin/login-test", "/admin/join")  // 이 경로는 검사 제외*/
//        ;
//    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해
                .allowedOriginPatterns("*")    // 모든 오리진 허용
                .allowedMethods("*")    // 모든 메소드 허용
                .maxAge(1800L);         // preflight 캐시
    }

}










