package com.dominest.dominestbackend.api.admin.controller;

import com.dominest.dominestbackend.api.admin.request.JoinRequest;
import com.dominest.dominestbackend.api.admin.response.JoinResponse;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import com.dominest.dominestbackend.api.admin.request.LoginRequest;
import com.dominest.dominestbackend.domain.jwt.dto.TokenDto;
import com.dominest.dominestbackend.domain.jwt.service.TokenManager;
import com.dominest.dominestbackend.domain.user.service.UserService;
import com.dominest.dominestbackend.global.apiResponse.ApiResponseDto;
import com.dominest.dominestbackend.global.apiResponse.ErrorStatus;
import com.dominest.dominestbackend.global.apiResponse.SuccessStatus;
import com.dominest.dominestbackend.global.exception.exceptions.auth.NotValidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final TokenManager tokenManager;

    @PostMapping("/join") // 회원가입
    public ResponseEntity<ApiResponseDto<JoinResponse>> signUp(@RequestBody @Valid final JoinRequest request){
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
//        if (existingUser != null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(ErrorStatus.USER_ALREADY_EXIST)); // 400 Bad Request 상태로 실패 응답 반환
//        }
        if(userService.checkDuplicateEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(ErrorStatus.EMAIL_ALREADY_EXIST));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(SuccessStatus.JOIN_SUCCESS, userService.create(request)));
    }

    @PostMapping("/login") // 로그인
    public ResponseEntity<ApiResponseDto<TokenDto>> login(@RequestBody @Valid final LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseDto.error(ErrorStatus.USER_CERTIFICATION_FAILED)); // 401 Unauthorized 상태로 실패 응답 반환
        }
        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseDto.error(ErrorStatus.USER_NOT_JOIN)); // 401 Unauthorized 상태로 실패 응답 반환
        }

        return ResponseEntity.ok(ApiResponseDto.success(SuccessStatus.LOGIN_SUCCESS, userService.login(request.getEmail(), request.getPassword())));
    }

    @GetMapping("/login-test") // accesstoken으로 유저정보 가져오기
    public ResponseEntity<ApiResponseDto<String>> loginTest(@RequestHeader(value = "Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String accessToken = authHeader.substring(7); // "Bearer " 접두사 제거
                if (tokenManager.validateToken(accessToken)) {
                    return ResponseEntity.ok(ApiResponseDto.success(SuccessStatus.TOKEN_USER_INFO, tokenManager.getMemberEmail(accessToken)));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseDto.error(ErrorStatus.USER_CERTIFICATION_FAILED)); // 401 Unauthorized 상태로 실패 응답 반환
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseDto.error(ErrorStatus.USER_CERTIFICATION_FAILED)); // 401 Unauthorized 상태로 실패 응답 반환
            }
        } catch (NotValidTokenException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseDto.error(ErrorStatus.USER_CERTIFICATION_FAILED)); // 401 Unauthorized 상태로 실패 응답 반환
        }
    }
}