package com.dominest.dominestbackend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${email.id}")
    private String username;

    @Value("${email.pw}")
    private String password;


    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com"); // Gmail SMTP 서버 주소
        javaMailSender.setPort(587); // TLS 포트 사용
        javaMailSender.setUsername(username); // Gmail 계정 이메일 주소
        javaMailSender.setPassword(password); // Gmail 계정 앱 비밀번호

        javaMailSender.setDefaultEncoding("utf-8");
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp"); // 프로토콜 설정
        properties.setProperty("mail.smtp.auth", "true"); // smtp 인증
        properties.setProperty("mail.smtp.starttls.enable", "true"); // TLS 사용
        properties.setProperty("mail.debug", "true"); // 디버그 사용

        return properties;
    }
}
//@Configuration
//public class EmailConfig {
//    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setHost("smtp.naver.com"); // smtp 서버 주소
//        javaMailSender.setPort(465); // 메일 인증 서버 포트
//        javaMailSender.setUsername("gjwldud0719"); // 아이디
//        javaMailSender.setPassword("jiyoung0343!"); // 비밀번호
//        javaMailSender.setDefaultEncoding("utf-8");
//        javaMailSender.setJavaMailProperties(getMailProperties()); // 메일 인증서버 정보 가져오기
//
//        return javaMailSender;
//    }
//
//    private Properties getMailProperties() {
//        Properties properties = new Properties();
//        properties.setProperty("mail.transport.protocol", "smtp"); // 프로토콜 설정
//        properties.setProperty("mail.smtp.auth", "true"); // smtp 인증
//        properties.setProperty("mail.smtp.starttls.enable", "true"); // smtp strattles 사용
//        properties.setProperty("mail.debug", "true"); // 디버그 사용
//        properties.setProperty("mail.smtp.ssl.trust","smtp.naver.com");
//        properties.setProperty("mail.smtp.ssl.enable","true"); // ssl 사용
//        return properties;
//    }
//
//}