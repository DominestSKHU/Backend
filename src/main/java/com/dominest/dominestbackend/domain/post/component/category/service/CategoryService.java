package com.dominest.dominestbackend.domain.post.component.category.service;

import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.categorygenerator.CategoryPositionGenerator;
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

    private final CategoryPositionGenerator positionGenerator;

    @Transactional
    public Category createCategory(String categoryName, Type categoryType, String explanation) {
        Category category = Category.builder()
                .name(categoryName)
                .type(categoryType)
                .explanation(explanation)
                .position(positionGenerator.getNextPosition())
                .build();

        return categoryRepository.save(category);
    }

    @Transactional // 카테고리 업데이트
    public void updateCategory(Long id, String categoryName, int position) throws Exception {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            throw new Exception(id + "번 카테고리를 찾을 수 없습니다.");
        }

        Category category = optionalCategory.get();
        category.updateCategory(categoryName);
        category.updatePosition(position);
    }

    @Transactional // 카테고리 삭제
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