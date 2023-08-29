package com.dominest.dominestbackend.api.category.controller;

import com.dominest.dominestbackend.api.category.request.CategoryCreateRequest;
import com.dominest.dominestbackend.api.category.request.CategoryUpdateRequest;
import com.dominest.dominestbackend.api.category.response.CategoryListDto;
import com.dominest.dominestbackend.api.category.response.CategoryListWithFavoriteDto;
import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.repository.CategoryRepository;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/check") // 카테고리 조회
    public ResTemplate<CategoryListDto.Res> handleGetCategoryList() {

        List<Category> categories = categoryRepository.findAll();
        CategoryListDto.Res resDto = CategoryListDto.Res.from(categories);
        return new ResTemplate<>(HttpStatus.OK, "카테고리 조회 성공", resDto);
    }

    // 현재 로그인한 사용자를 기준으로 즐겨찾기 여부와 함께 카테고리 조회
    @GetMapping("/my-categories")
    public ResTemplate<CategoryListWithFavoriteDto.Res> handleGetMyCategoryList(@NotNull(message = "인증 정보가 없습니다.") Principal principal) {
        // 즐찾목록 다 조회해서 카테고리 ID들을 찾아낸다.
        // 찾아낸 카테고리 ID들과 전체 카테고리 목록 중 일치하는 것들은 즐겨찾기가 되어있는 것이다.
        List<Long> categoryIdsFromFavorites = categoryService.getIdAllByUserEmail(principal.getName());
        List<Category> categories = categoryRepository.findAll();

        CategoryListWithFavoriteDto.Res resDto = CategoryListWithFavoriteDto.Res.from(categories, categoryIdsFromFavorites);
        return new ResTemplate<>(HttpStatus.OK, "카테고리 조회 성공", resDto);
    }

    @PostMapping ("/create") // 카테고리 생성
    public ResponseEntity<ResTemplate<?>> createCategory(@RequestBody @Valid final CategoryCreateRequest request) {

        Category category = categoryService.createCategory(request.getCategoryName(), request.getCategoryType(), request.getExplanation());
        ResTemplate<?> resTemplate = new ResTemplate<>(HttpStatus.CREATED, "카테고리 생성 성공");
        return ResponseEntity
                .created(URI.create("/categories/" + category.getId()))
                .body(resTemplate);
    }

    @PutMapping("/update") // 카테고리 수정
    public ResTemplate<String> updateCategories(@RequestBody @Valid final List<CategoryUpdateRequest> requests) throws Exception {

        for (CategoryUpdateRequest request : requests) {
            categoryService.updateCategory(request.getId(), request.getCategoryName(), request.getPosition());
        }
        return new ResTemplate<>(HttpStatus.OK, "카테고리 수정 성공");
    }

    @DeleteMapping("/delete/{id}") // 카테고리 삭제
    public ResTemplate<String> deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategoryById(id);
        return new ResTemplate<>(HttpStatus.OK, id +"번 카테고리 삭제 성공");
    }
}