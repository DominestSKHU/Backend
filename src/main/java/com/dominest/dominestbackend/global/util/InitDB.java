package com.dominest.dominestbackend.global.util;


import com.dominest.dominestbackend.domain.favorite.Favorite;
import com.dominest.dominestbackend.domain.favorite.FavoriteRepository;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.repository.CategoryRepository;
import com.dominest.dominestbackend.domain.post.image.ImageType;
import com.dominest.dominestbackend.domain.post.image.ImageTypeRepository;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.component.Role;
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
    private final FavoriteRepository favoriteRepository;

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



        Category category1 = Category.builder()
                .name("장기 미수령 택배 관리대장")
                .type(Type.UNDELIVERED_PARCEL_REGISTER)
                .explanation("장미택관")
                .build();
        categoryRepository.save(category1);

        Category category2 = Category.builder()
                .name("이미지타입1")
                .type(Type.IMAGE)
                .explanation("explanation")
                .build();
        categoryRepository.save(category2);



        ArrayList<Category> categories = new ArrayList<>();
        int categoryCount = 7;
        for (int i = 1; i <= categoryCount; i++) {
            Category category = Category.builder()
                    .name("categoryName" + i)
                    .type(Type.IMAGE)
                    .explanation("explanation")
                    .build();
            categories.add(category);
        }
        categoryRepository.saveAll(categories);



        ArrayList<ImageType> imageTypes = new ArrayList<>();
        int postCount = 100;
        for (int i = 1; i <= postCount; i++) {
            ImageType imageType = ImageType.builder()
                    .title("title" + i)
                    .writer(user)
                    .category(category2)
                    .build();
            imageTypes.add(imageType);
        }
        imageTypeRepository.saveAll(imageTypes);

        ArrayList<Favorite> favorites = new ArrayList<>();
        for (int i = 1; i <= categoryCount; i++) {
            Favorite favorite = Favorite.builder()
                    .user(user)
                    .category(categories.get(i - 1))
                    .build();
            favorites.add(favorite);
        }
        favoriteRepository.saveAll(favorites);
    }
}
