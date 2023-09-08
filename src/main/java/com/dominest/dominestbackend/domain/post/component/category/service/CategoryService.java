package com.dominest.dominestbackend.domain.post.component.category.service;

import com.dominest.dominestbackend.api.category.request.CategoryUpdateRequest;
import com.dominest.dominestbackend.domain.post.complaint.ComplaintRepository;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.repository.CategoryRepository;
import com.dominest.dominestbackend.domain.post.image.ImageTypeRepository;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.UndeliveredParcelPostRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import com.dominest.dominestbackend.global.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ImageTypeRepository imageTypeRepository;
    private final UndeliveredParcelPostRepository undeliveredParcelPostRepository;
    private final ComplaintRepository complaintRepository;

    @Transactional
    public Category create(String categoryName, Type categoryType, String explanation) {

        Category category = Category.builder()
                .name(categoryName)
                .type(categoryType)
                .explanation(explanation)
                .orderKey(categoryRepository.getNewOrderKey())
                .build();

        try {
            return categoryRepository.save(category); // Identity 전략이므로 즉시 flush
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("카테고리 저장 실패, name 중복 혹은 값의 누락을 확인해주세요", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @return 변경 요청이 들어온 카테고리의 개수
     */
    @Transactional
    public int update(CategoryUpdateRequest reqDto) {
        // forEach, update
        reqDto.getCategories().forEach(
                categoryDto -> {
            Category category = EntityUtil.mustNotNull(categoryRepository.findById(categoryDto.getId()), ErrorCode.CATEGORY_NOT_FOUND);
            category.updateValues(categoryDto.getCategoryName(), categoryDto.getExplanation(), categoryDto.getOrderKey());
            }
        );

        // 중복 체크, 조회 시점에서 flush 되었을 것.
        List<Integer> orderKeys = categoryRepository.findAllOrderKey();
        Set<Integer> set = new HashSet<>();

        orderKeys.forEach(key -> {
            // If this set already contains the element, the call leaves the set unchanged and returns false
            // nullable false. orderKeys에 null 없음
            if (!set.add(key)) {
                throw new BusinessException(ErrorCode.CATEGORY_ORDER_KEY_DUPLICATED);
            }
        });

        return reqDto.getCategories().size();
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
        Category category = EntityUtil.mustNotNull(categoryRepository.findById(categoryId), ErrorCode.CATEGORY_NOT_FOUND);

        deletePosts(categoryId, category.getType()); // 카테고리를 참조하는 연관 게시글 함께 삭제

        categoryRepository.delete(category);
    }

    private void deletePosts(Long categoryId, Type type) {
        if (Type.IMAGE.equals(type)) {
            imageTypeRepository.deleteByCategoryId(categoryId);
        } else if (Type.UNDELIVERED_PARCEL_REGISTER.equals(type)) {
            undeliveredParcelPostRepository.deleteByCategoryId(categoryId);
        } else if (Type.COMPLAINT.equals(type)) {
            complaintRepository.deleteByCategoryId(categoryId);
        }
    }

    public Category validateCategoryType(Long categoryId, Type type) {
        Category category = getById(categoryId);
        category.getType().validateEqualTo(type);
        return category;
    }
}





















