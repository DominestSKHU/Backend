package com.dominest.dominestbackend.api.admin.controller;

import com.dominest.dominestbackend.api.admin.request.ChangePasswordRequest;
import com.dominest.dominestbackend.api.admin.request.JoinRequest;
import com.dominest.dominestbackend.api.admin.request.LoginRequest;
import com.dominest.dominestbackend.api.admin.response.JoinResponse;
import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.domain.jwt.dto.TokenDto;
import com.dominest.dominestbackend.domain.jwt.service.TokenValidator;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import com.dominest.dominestbackend.domain.user.service.UserService;
import com.dominest.dominestbackend.global.apiResponse.ApiResponseDto;
import com.dominest.dominestbackend.global.apiResponse.SuccessStatus;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import com.dominest.dominestbackend.global.util.EntityUtil;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenValidator tokenValidator;

    @PostMapping("/join") // 회원가입
    public ResponseEntity<JoinResponse> signUp(@RequestBody @Valid final JoinRequest request){
        JoinResponse joinResponse = userService.create(request);
        return ResponseEntity.status(HttpStatus.OK).body(joinResponse);
    }

    @PostMapping("/login") // 로그인
    public RspTemplate<TokenDto> login(@RequestBody @Valid final LoginRequest request) {
        TokenDto tokenDto = userService.loginTemp(request.getEmail(), request.getPassword());

        return new RspTemplate<>(HttpStatus.OK, "로그인 성공", tokenDto);
    }

    @PostMapping("/login/short-token-exp") // 로그인
    public RspTemplate<TokenDto> loginV2(@RequestBody @Valid final LoginRequest request) {
        TokenDto tokenDto = userService.login(request.getEmail(), request.getPassword());

        return new RspTemplate<>(HttpStatus.OK, "로그인 성공", tokenDto);
    }

    // 로그아웃
    @PostMapping("/logout")
    public RspTemplate<Void> logout(Principal principal) {
        // 액세스 토큰 검증은 필터에서 거치므로 바로 로그아웃 처리
        userService.logout(PrincipalUtil.toEmail(principal));

        return new RspTemplate<>(HttpStatus.OK, "로그아웃 성공");
    }

    /**
     *   refresh 토큰을 이용, access 토큰을 재발급하는 메소드
     */
    @PostMapping(value = "/token/reissue")
    public RspTemplate<TokenDto> accessToken(HttpServletRequest httpServletRequest){

        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        tokenValidator.validateBearer(authorizationHeader);

        String refreshToken = authorizationHeader.split(" ")[1];
        TokenDto tokenDto = userService.reassureByRefreshToken(refreshToken);

        return new RspTemplate<>(HttpStatus.OK, "토큰 재발급", tokenDto);
    }

    @PostMapping("/myPage/password") // 비밀번호 변경
    public ResponseEntity<ApiResponseDto<Void>> changePassword(@RequestBody ChangePasswordRequest request
                                                                                                                , Principal principal) {
        String logInUserEmail = PrincipalUtil.toEmail(principal);
        User user = EntityUtil.mustNotNull(userRepository.findByEmail(logInUserEmail), ErrorCode.USER_NOT_FOUND);

        // 현재 비밀번호와 입력한 비밀번호가 일치하는지 확인
        if (userService.validateUserPassword(request.getPassword(), user.getPassword())) {
            user.changePassword(passwordEncoder.encode(request.getNewPassword())); // 비밀번호 변경
            userRepository.save(user);
            return ResponseEntity.ok(ApiResponseDto.success(SuccessStatus.CHANGE_PASSWORD_SUCCESS));
        } else {
            throw new BusinessException(ErrorCode.EMAIL_VERIFICATION_CODE_MISMATCHED);
        }
    }
}