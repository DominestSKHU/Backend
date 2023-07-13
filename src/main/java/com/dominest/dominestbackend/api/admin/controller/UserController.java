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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final TokenManager tokenManager;

    @PostMapping("/join")
    // @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseDto<JoinResponse> signUp(@RequestBody @Valid final JoinRequest request){
        return ApiResponseDto.success(SuccessStatus.JOIN_SUCCESS, userService.create(request));
    }

    @PostMapping("/login")
    public ApiResponseDto<TokenDto> login(@RequestBody @Valid final LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ApiResponseDto.error(ErrorStatus.USER_CERTIFICATION_FAILED);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ApiResponseDto.error(ErrorStatus.USER_NOT_JOIN);
        }

        TokenDto tokenDto = userService.login(request.getEmail(), request.getPassword());
        return ApiResponseDto.success(SuccessStatus.LOGIN_SUCCESS, tokenDto);
    }


    @GetMapping("/login-test")
    public ApiResponseDto<String> loginTest(@RequestHeader(value = "Authorization") String accessToken) {
        try {
            if (accessToken != null && tokenManager.validateToken(accessToken)) {
                return ApiResponseDto.success(SuccessStatus.TOKEN_USER_INFO, tokenManager.getMemberEmail(accessToken));
            } else {
                return ApiResponseDto.error(ErrorStatus.USER_CERTIFICATION_FAILED);
            }
        } catch (NotValidTokenException e) {
            e.printStackTrace();
            return ApiResponseDto.error(ErrorStatus.USER_CERTIFICATION_FAILED);
        }
    }
}