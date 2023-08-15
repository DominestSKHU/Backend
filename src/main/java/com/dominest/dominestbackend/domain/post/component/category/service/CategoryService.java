package com.dominest.dominestbackend.domain.post.component.category.service;

import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.repository.CategoryRepository;
import com.dominest.dominestbackend.domain.post.component.category.component.categorytype.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

//    @Transactional
//    public void createCategoryByType(String categoryName, String categoryType, Optional<User> creator) {
//        // 카테고리 종류 생성
//        CategoryType type = CategoryType.builder()
//                .type(categoryType)
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
    public Category createCategory(String categoryName, Type categoryType, String explanation, String name) {
        Category category = Category.builder()
                .categoryName(categoryName)
                .categoryType(categoryType)
                .explanation(explanation)
                .name(name)
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
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalStateException("해당 카테고리가 존재하지 않습니다."));
    }

    @Transactional
    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
