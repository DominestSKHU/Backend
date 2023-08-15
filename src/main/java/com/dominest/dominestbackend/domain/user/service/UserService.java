package com.dominest.dominestbackend.domain.user.service;


import com.dominest.dominestbackend.api.admin.request.JoinRequest;
import com.dominest.dominestbackend.api.admin.response.JoinResponse;
import com.dominest.dominestbackend.domain.jwt.dto.TokenDto;
import com.dominest.dominestbackend.domain.jwt.service.TokenManager;
import com.dominest.dominestbackend.domain.role.Role;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenManager tokenManager;

    @Transactional
    public JoinResponse create(JoinRequest request){
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.ROLE_ADMIN) // Fixme 현재 모든 가입자는 관리자로 고정됨.
                .build();

        userRepository.save(user);

        return JoinResponse.of(user.getEmail(), user.getName(), user.getPhoneNumber());
    }

    public TokenDto login(String email, String password){
        // 현재 credential과 Authentication 객체를 토큰 생성 시 사용하지 않아 주석 처리함
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return tokenManager.createTokenDto(email);
    }


    public boolean checkDuplicateEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean validateUserPassword(String currentPassword, String loggedInUserPassword) {
        return passwordEncoder.matches(currentPassword, loggedInUserPassword);
    }

//    public Optional<User> getUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
    public User getUserByEmail(String email) {
        return EntityUtil.checkNotFound(userRepository.findByEmail(email), ErrorCode.USER_NOT_FOUND);
    }
}