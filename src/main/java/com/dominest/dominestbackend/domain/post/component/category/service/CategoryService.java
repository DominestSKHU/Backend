package com.dominest.dominestbackend.domain.post.component.category.service;

import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.repository.CategoryRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

//    @Transactional
//    public void createCategoryByType(String categoryName, String type, Optional<User> creator) {
//        // 카테고리 종류 생성
//        CategoryType type = CategoryType.builder()
//                .type(type)
//                .build();
//
//        // 카테고리 생성
//        Category category = Category.builder()
//                .categoryName(categoryName)
//                .createdBy(creator.orElse(null))
//                .build();
//
//        // 카테고리 - 카테고리 타입 연결
//        category.connectCategoryType(type);
//
//        categoryRepository.save(category);
//    }

    @Transactional
    public Category createCategory(String categoryName, Type categoryType, String explanation) {
        Category category = Category.builder()
                .name(categoryName)
                .type(categoryType)
                .explanation(explanation)
                .build();

        return categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(Long id, String categoryName) throws Exception {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            throw new Exception("찾을 수 업서");
        }

        Category category = optionalCategory.get();
        category.updateCategory(categoryName);
    }

    @Transactional
    public void deleteCategory(Long categoryId) throws Exception {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isEmpty()) {
            throw new Exception("삭제할 카테고리가 존재하지 않습니다.");
        }

        Category category = optionalCategory.get();
        categoryRepository.delete(category);
    }

    public Category getCategoryById(Long categoryId) {
        return EntityUtil.checkNotFound(categoryRepository.findById(categoryId), ErrorCode.CATEGORY_NOT_FOUND);
    }

    public List<Long> getIdAllByUserEmail(String email) {
        // Favorite 되어있는 카테고리 ID 전체 조회
        return categoryRepository.findIdAllByUserEmailFetchCategory(email);
    }

    @Transactional
    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}





















