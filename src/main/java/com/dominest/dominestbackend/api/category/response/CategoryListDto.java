package com.dominest.dominestbackend.api.category.response;

import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryListDto {
    @Getter
    public static class Res {
        List<CategoryDto> categories;

        public static Res from(List<Category> categories){
            return new Res(CategoryDto.from(categories));
        }
        Res(List<CategoryDto> categories){
            this.categories = categories;
        }

        @Builder
        @Getter
        private static class CategoryDto{
            long id;
            int orderKey;
            String name; // 카테고리 이름
            Type type;
            String explanation; // 카테고리 상세설명
            String categoryLink; // 카테고리 게시글 목록으로 이동하는 링크

            static CategoryDto from(Category category) {
                return CategoryDto.builder()
                        .id(category.getId())
                        .orderKey(category.getOrderKey())
                        .name(category.getName())
                        .type(category.getType())
                        .explanation(category.getExplanation())
                        .categoryLink(category.getPostsLink())
                        .build();
            }

            static List<CategoryDto> from(List<Category> categories) {
                return categories.stream()
                        .map(CategoryDto::from)
                        .collect(Collectors.toList());
            }
        }
    }



}
