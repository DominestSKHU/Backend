package com.dominest.dominestbackend.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Custom401AuthEntryPoint custom401AuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable() // 폼 로그인 비활성화. JWT 필터로 대체
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeRequests()
                /*
                permitAll()은 필터링 예외가 아니라, 필터링을 모두 거치고 나서 Auth 객체가 없어도 접근을 허용하는 것이다.
                그래서 GET 요청에서도 SecurityContext의 Authentication 객체를 활용 가능하다. 물론 해당 객체는 null일 수도 있다.
                */
                .antMatchers(HttpMethod.GET).permitAll() // GET 요청은 핕터링
                .antMatchers(HttpMethod.OPTIONS).permitAll() // OPTIONS 요청은 토큰 검증 예외
                .antMatchers("/user/join").permitAll() // 회원가입 요청은 토큰 검증 예외
                .antMatchers("/user/login/**").permitAll()
                .antMatchers("/user/token/reassure").permitAll()

                .antMatchers("/email/send").permitAll()
                .antMatchers("/email/verify/code").permitAll()
                .antMatchers("/email/change/password").permitAll()
                .anyRequest().authenticated()
                .and()

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(custom401AuthEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)

        ; // 인증 예외
        return http.build();
    }


}