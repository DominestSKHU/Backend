package com.dominest.dominestbackend.domain.post.manual;

import com.dominest.dominestbackend.api.post.manual.dto.CreateManualPostDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ManualPostService {
    private final CategoryService categoryService;
    private final UserService userService;
    private final ManualPostRepository manualPostRepository;

    @Transactional
    public Long create(Long categoryId, CreateManualPostDto.Req reqDto, String email) {
        Category category = categoryService.validateCategoryType(categoryId, Type.MANUAL);
        User user = userService.getUserByEmail(email);
        ManualPost manualPost = ManualPost.builder().
                title(reqDto.getTitle()).
                writer(user).
                category(category).
                htmlContent(reqDto.getHtmlContent()).
                build();

        return manualPostRepository.save(manualPost).getId();
    }
}
