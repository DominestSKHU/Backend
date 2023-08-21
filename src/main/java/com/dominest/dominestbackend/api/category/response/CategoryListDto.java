package com.dominest.dominestbackend.api.category.response;

import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryListDto {
    private List<CategoryDto> categories;

    public static CategoryListDto from(List<Category> categories){
        List<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryDto::from)
                .collect(Collectors.toList());
        return new CategoryListDto(categoryDtos);
    }
    private CategoryListDto(List<CategoryDto> categories){
        this.categories = categories;
    }

    @Builder
    @Getter
    private static class CategoryDto{
        private Long id;
        private String name; // 카테고리 이름
        private Type type;
        private String explanation; // 카테고리 상세설명

        public static CategoryDto from(Category category) {
            return CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .type(category.getType())
                    .explanation(category.getExplanation())
                    .build();
        }
    }


}
