package com.dominest.dominestbackend.global.util;


import com.dominest.dominestbackend.domain.category.Category;
import com.dominest.dominestbackend.domain.category.repository.CategoryRepository;
import com.dominest.dominestbackend.domain.categorytype.Type;
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
    private final CategoryRepository categoryRepository;

    @Transactional
    @PostConstruct
    public void init() {
        User user = User.builder()
                .email("eeee@email.com")
                .password(passwordEncoder.encode("pppp"))
                .name("name")
                .phoneNumber("010-1234-5678")
                .build();
        userRepository.save(user);

        Category category = Category.builder()
                .categoryName("categoryName")
                .categoryType(Type.WRITE_POST)
                .explanation("explanation")
                .name("name")
                .build();
        categoryRepository.save(category);
    }
}
