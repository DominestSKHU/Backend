package com.dominest.dominestbackend.domain.email.controller;

import com.dominest.dominestbackend.domain.email.request.EmailRequest;
import com.dominest.dominestbackend.domain.email.service.EmailService;
import com.dominest.dominestbackend.domain.email.service.EmailVerificationService;
import com.dominest.dominestbackend.global.apiResponse.ApiResponseDto;
import com.dominest.dominestbackend.global.apiResponse.ErrorStatus;
import com.dominest.dominestbackend.global.apiResponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send") // 인증번호 발송 버튼 누르면 메일 가게
    public ResponseEntity<ApiResponseDto<String>> sendEmail(@RequestBody EmailRequest emailRequest) throws Exception {
        emailService.sendJoinMessage(emailRequest.getEmail()); // 이메일로 인증코드를 보낸다.
        return ResponseEntity.ok(ApiResponseDto.success(SuccessStatus.SEND_EMAIL_SUCCESS, emailRequest.getEmail() + "로 검증코드를 전송했습니다."));
    }


    @PostMapping("/change/password") // 임시 비밀번호 이메일 전송
    public ResponseEntity<ApiResponseDto<String>> changePasswordEmail(@RequestBody EmailRequest emailRequest) throws Exception {
        emailService.sendChangeMessage(emailRequest.getEmail()); // 이메일로 인증코드를 보냄
        return ResponseEntity.ok(ApiResponseDto.success(SuccessStatus.SEND_EMAIL_SUCCESS, emailRequest.getEmail() + "로 검증코드를 전송했습니다."));
    }


    @PostMapping("/verify/code") // 이메일 인증코드 검증
    public ResponseEntity<ApiResponseDto<String>> verifyEmail(@RequestBody EmailRequest emailRequest) {
        if (emailVerificationService.verifyCode(emailRequest.getEmail(), emailRequest.getCode())) { // 인증 성공
            return ResponseEntity.ok(ApiResponseDto.success(SuccessStatus.VERIFY_EMAIL_SUCCESS)); // 이메일과 함께 성공 응답 반환
        } else {
            // 인증 실패
            System.out.println(emailVerificationService.verifyCode(emailRequest.getEmail(), emailRequest.getCode()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(ErrorStatus.VERIFY_EMAIL_FAILED)); // 400 Bad Request 상태로 실패 응답 반환
        }
    }
}