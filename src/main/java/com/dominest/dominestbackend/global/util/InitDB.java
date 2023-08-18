package com.dominest.dominestbackend.global.util;


import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.repository.CategoryRepository;
import com.dominest.dominestbackend.domain.post.component.category.component.categorytype.Type;
import com.dominest.dominestbackend.domain.post.image.ImageType;
import com.dominest.dominestbackend.domain.post.image.ImageTypeRepository;
import com.dominest.dominestbackend.domain.role.Role;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class InitDB {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ImageTypeRepository imageTypeRepository;

    @Transactional
    @PostConstruct
    public void init() {
        User user = User.builder()
                .email("eeee@email.com")
                .password(passwordEncoder.encode("pppp"))
                .name("name1")
                .phoneNumber("010-1234-5678")
                .role(Role.ROLE_ADMIN)
                .build();
        User user2 = User.builder()
                .email("eeee1@email.com")
                .password(passwordEncoder.encode("pppp"))
                .name("name2")
                .phoneNumber("010-1235-5678")
                .role(Role.ROLE_ADMIN)
                .build();
        User user3 = User.builder()
                .email("eeee2@email.com")
                .password(passwordEncoder.encode("pppp"))
                .name("name3")
                .phoneNumber("010-1236-5678")
                .role(Role.ROLE_ADMIN)
                .build();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Category category = Category.builder()
                .categoryName("categoryName")
                .categoryType(Type.IMAGE)
                .explanation("explanation")
                .build();
        categoryRepository.save(category);
        Category category2 = Category.builder()
                .categoryName("categoryName2")
                .categoryType(Type.TEXT_AND_IMAGE)
                .explanation("explanation")
                .build();
        categoryRepository.save(category2);

        ArrayList<ImageType> imageTypes = new ArrayList<>();
        int postCount = 100;
        for (int i = 1; i <= postCount; i++) {
            ImageType imageType = ImageType.builder()
                    .title("title")
                    .writer(user)
                    .category(category)
                    .build();
            imageTypes.add(imageType);
        }
        imageTypeRepository.saveAll(imageTypes);
    }
}
