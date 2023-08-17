package com.dominest.dominestbackend.api.category.controller;

import com.dominest.dominestbackend.api.category.request.CategoryCreateRequest;
import com.dominest.dominestbackend.api.category.request.CategoryUpdateRequest;
import com.dominest.dominestbackend.api.category.response.CategoryListDto;
import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.repository.CategoryRepository;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @GetMapping // 카테고리 조회
    public ResTemplate<CategoryListDto> handleGetCategoryList() {

        List<Category> categories = categoryRepository.findAll();
        CategoryListDto categoryDtoList = CategoryListDto.from(categories);
        return new ResTemplate<>(HttpStatus.OK, "카테고리 조회 성공", categoryDtoList);
    }

    @PostMapping // 카테고리 생성
    public ResponseEntity<ResTemplate<?>> createCategory(@RequestBody @Valid final CategoryCreateRequest request) {

        Category category = categoryService.createCategory(request.getCategoryName(), request.getCategoryType(), request.getExplanation());
        ResTemplate<?> resTemplate = new ResTemplate<>(HttpStatus.CREATED, "카테고리 생성 성공");
        return ResponseEntity
                .created(URI.create("/categories/" + category.getId()))
                .body(resTemplate);
    }

    @PutMapping // 카테고리 수정
    public ResTemplate<String> updateCategories(@RequestBody @Valid final List<CategoryUpdateRequest> requests) throws Exception {

        for (CategoryUpdateRequest request : requests) {
            categoryService.updateCategory(request.getId(), request.getCategoryName());
        }
        return new ResTemplate<>(HttpStatus.OK, "카테고리 수정 성공");
    }

    @DeleteMapping("/{id}") // 카테고리 삭제
    public ResTemplate<String> deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategoryById(id);
        return new ResTemplate<>(HttpStatus.OK, id +"번 카테고리 삭제 성공");
    }
}