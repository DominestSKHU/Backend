package com.dominest.dominestbackend.domain.email.controller;

import com.dominest.dominestbackend.domain.email.request.CodeRequest;
import com.dominest.dominestbackend.domain.email.service.EmailService;
import com.dominest.dominestbackend.global.apiResponse.ApiResponseDto;
import com.dominest.dominestbackend.global.apiResponse.ErrorStatus;
import com.dominest.dominestbackend.global.apiResponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    private final HttpSession session;

    @PostMapping("/send") // 인증번호 발송 버튼 누르면 메일 가게
    public ResponseEntity<ApiResponseDto<String>> mailConfirm(@RequestBody EmailRequest emailRequest) throws Exception{
        String code = emailService.sendSimpleMessage(emailRequest.getEmail());
        session.setAttribute("emailCode", code);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessStatus.SEND_EMAIL_SUCCESS));
    }

    @PostMapping("verify/code")
    public ResponseEntity<ApiResponseDto<String>> verifyEmail(@RequestBody CodeRequest request) {
        session.setMaxInactiveInterval(30 * 60);
        String sessionCode = (String) session.getAttribute("emailCode");
        if (sessionCode != null && sessionCode.equals(request.getCode())) { // 인증 성공
            return ResponseEntity.ok(ApiResponseDto.success(SuccessStatus.VERIFY_EMAIL_SUCCESS)); // 이메일과 함께 성공 응답 반환
        } else {
            // 인증 실패
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(ErrorStatus.VERIFY_EMAIL_FAILED)); // 400 Bad Request 상태로 실패 응답 반환
        }
    }

}
