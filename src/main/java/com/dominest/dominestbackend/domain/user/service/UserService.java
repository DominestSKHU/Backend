package com.dominest.dominestbackend.domain.user.service;


import com.dominest.dominestbackend.api.admin.request.JoinRequest;
import com.dominest.dominestbackend.api.admin.response.JoinResponse;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import com.dominest.dominestbackend.domain.jwt.dto.TokenDto;
import com.dominest.dominestbackend.domain.jwt.service.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenManager tokenManager;

    @Transactional
    public JoinResponse create(JoinRequest request){
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        return JoinResponse.of(user.getEmail(), user.getPassword());
    }

    public TokenDto login(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        TokenDto tokenDto = tokenManager.createTokenDto(String.valueOf(authenticationToken));

        return tokenDto;
    }
}