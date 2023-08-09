package com.dominest.dominestbackend.api.category.controller;

import com.dominest.dominestbackend.api.category.request.CategoryCreateRequest;
import com.dominest.dominestbackend.api.category.request.CategoryUpdateRequest;
import com.dominest.dominestbackend.api.category.response.CategoryListDto;
import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.domain.category.Category;
import com.dominest.dominestbackend.domain.category.repository.CategoryRepository;
import com.dominest.dominestbackend.domain.category.service.CategoryService;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @GetMapping // 카테고리 조회
    public ResTemplate<CategoryListDto> handleGetCategoryList() {
        List<Category> categories = categoryRepository.findAll();
        CategoryListDto categoryDtoList = CategoryListDto.from(categories);
        return new ResTemplate<>(HttpStatus.OK, "카테고리 조회 성공", categoryDtoList);
    }


    @PostMapping // 카테고리 생성
    public ResponseEntity<ResTemplate<?>> createCategory(@RequestBody @Valid final CategoryCreateRequest request, Authentication authentication) {
        try {
            String logInUserEmail = SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal().toString().split(",")[0].split("=")[1]; // email 주소 가져오기

            Optional<User> creator = userService.getUserByEmail(logInUserEmail);

            categoryService.createCategory(request.getCategoryName(), request.getCategoryType(), request.getExplanation(), creator.get().getName());

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_CATEGORY_CREATE);
        }
    }

    @PutMapping // 카테고리 수정
    public ResponseEntity<String> updateCategories(@RequestBody @Valid final List<CategoryUpdateRequest> requests,
                                                   Authentication authentication) {
        try {
            // 로그인한 사용자의 이름 문자열 가져오기
            String logInUserName = SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal().toString().split(",")[0].split("=")[1];

            Optional<User> creator = userService.getUserByEmail(logInUserName);

            for (CategoryUpdateRequest request : requests) {
                Category category = categoryService.getCategoryById(request.getId());

                categoryService.updateCategory(request.getId(), request.getCategoryName());
                category.updateEditUser(creator.get().getName());
            }

            return new ResponseEntity<>("카테고리 업데이트 성공~~", HttpStatus.OK);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_UPDATE_CATEGORY);
        }
    }

    @DeleteMapping("/{id}") // 카테고리 삭제
    public ResponseEntity<String> deleteCategory(@PathVariable Long id, Authentication authentication) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                categoryService.deleteCategoryById(id);
                return new ResponseEntity<>("카테고리가 성공적으로 삭제되었습니다.", HttpStatus.OK);
            } else {
                throw new BusinessException(ErrorCode.NO_ACCESS_USER);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_DELETE_CATEGORY);
        }
    }

}