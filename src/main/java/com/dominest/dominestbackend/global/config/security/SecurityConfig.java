package com.dominest.dominestbackend.global.config.security;

import com.dominest.dominestbackend.domain.jwt.service.JwtAuthenticationFilter;
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
                .antMatchers(HttpMethod.GET).permitAll() // GET 요청은 토큰 검증 예외
                .antMatchers(HttpMethod.OPTIONS).permitAll() // OPTIONS 요청은 토큰 검증 예외
                .antMatchers("/user/join").permitAll() // 회원가입 요청은 토큰 검증 예외
                .antMatchers("/user/login").permitAll()
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