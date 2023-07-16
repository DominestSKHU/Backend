package com.dominest.dominestbackend.global.apiResponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorStatus {

    /*
    BAD_REQUEST
     */
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_LOGIN_STATUS(HttpStatus.BAD_REQUEST, "로그인 되어있지 않습니다."),
    VALIDATION_REQUEST_MISSING_EXCEPTION(HttpStatus.BAD_REQUEST, "요청값이 입력되지 않았습니다."),
    USER_CERTIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "해당 아이디나 비밀번호를 가진 유저가 존재하지 않습니다."),
    USER_NOT_JOIN(HttpStatus.FORBIDDEN, "해당 사용자가 존재하지 않습니다."),

    /*
    SERVER_ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생했습니다."),
    BAD_GATEWAY_EXCEPTION(HttpStatus.BAD_GATEWAY, "일시적인 에러가 발생하였습니다.\n잠시 후 다시 시도해주세요");

    private final HttpStatus httpStatus;
    private final String message;
}