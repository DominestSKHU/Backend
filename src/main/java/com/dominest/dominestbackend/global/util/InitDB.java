package com.dominest.dominestbackend.global.util;


import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class InitDB {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    @PostConstruct
    public void init() {
        User user = User.builder()
                .email("eeee@email.com")
                .password(passwordEncoder.encode("pppp"))
                .build();
        User user2 = User.builder()
                .email("eeee1@email.com")
                .password(passwordEncoder.encode("pppp"))
                .build();
        User user3 = User.builder()
                .email("eeee2@email.com")
                .password(passwordEncoder.encode("pppp"))
                .build();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}
