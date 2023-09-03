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

    @Transactional
    public Category create(String categoryName, Type categoryType, String explanation) {
        Category category = Category.builder()
                .name(categoryName)
                .type(categoryType)
                .explanation(explanation)
                .build();

        return categoryRepository.save(category);
    }

    @Transactional
    public void update(Long id, String categoryName) throws Exception {
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

    public Category getById(Long categoryId) {
        return EntityUtil.mustNotNull(categoryRepository.findById(categoryId), ErrorCode.CATEGORY_NOT_FOUND);
    }

    public List<Long> getIdAllByUserEmail(String email) {
        // Favorite 되어있는 카테고리 ID 전체 조회
        return categoryRepository.findIdAllByUserEmailFetchCategory(email);
    }

    @Transactional
    public void deleteById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public Category validateCategoryType(Long categoryId, Type type) {
        Category category = getById(categoryId);
        category.getType().validateEqualTo(type);
        return category;
    }
}





















