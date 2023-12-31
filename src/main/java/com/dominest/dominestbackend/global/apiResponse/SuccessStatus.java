package com.dominest.dominestbackend.global.apiResponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessStatus {

    JOIN_SUCCESS(HttpStatus.CREATED, "회원가입 성공!"),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공!"),

    SEND_EMAIL_SUCCESS(HttpStatus.OK, "이메일 전송 성공!"),

    VERIFY_EMAIL_SUCCESS(HttpStatus.OK, "이메일 인증 성공!"),

    TOKEN_USER_INFO(HttpStatus.OK, "accessToken으로 유저 정보 가져오기 성공!!"),
    CHANGE_PASSWORD_SUCCESS(HttpStatus.OK, "비밀번호 변경 성공");

    private final HttpStatus httpStatus;
    private final String message;
}