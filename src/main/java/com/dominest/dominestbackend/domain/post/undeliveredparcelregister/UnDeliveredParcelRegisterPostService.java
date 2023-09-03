package com.dominest.dominestbackend.domain.post.undeliveredparcelregister;

import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UnDeliveredParcelRegisterPostService {
    private final UnDeliveredParcelRegisterPostRepository unDeliParcelPostRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    @Transactional
    public Long create(Long categoryId, String email) {
        // Undeli...의 연관 객체인 category, user 찾기
        User user = userService.getUserByEmail(email);
        Category category = categoryService.getCategoryById(categoryId);

        // Undeli... 객체 생성 후 저장
        UnDeliveredParcelRegisterPost unDeliParcelPost = UnDeliveredParcelRegisterPost.builder()
                .titleWithCurrentDate(createTitle())
                .category(category)
                .writer(user)
                .build();
        return unDeliParcelPostRepository.save(unDeliParcelPost).getId();
    }

    private String createTitle() {
        // 원하는 형식의 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(formatter);
        return formattedDate + " 장기미수령 택배";
    }
}
