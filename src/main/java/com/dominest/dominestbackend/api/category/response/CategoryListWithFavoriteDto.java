package com.dominest.dominestbackend.api.category.response;

import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


public class CategoryListWithFavoriteDto {
    @Getter
    public static class Res {
        List<CategoryDto> categories;

        public static Res from(List<Category> categories, List<Long> favoriteIds){
            return new Res(CategoryDto.from(categories, favoriteIds));
        }
        Res(List<CategoryDto> categories){
            this.categories = categories;
        }

        @Builder
        @Getter
        private static class CategoryDto{
            Long id;
            String name; // 카테고리 이름
            Type type;  // 타입
            boolean favorite; // 즐겨찾기 여부
            String categoryLink;

            static CategoryDto from(Category category, List<Long> favoriteIds) {
                return CategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .type(category.getType())
                        .favorite(favoriteIds.contains(category.getId()))
                        .categoryLink(category.getPostsLink())
                        .build();
            }

            static List<CategoryDto> from(List<Category> categories, List<Long> favoriteIds) {
                return categories.stream()
                        .map(category -> CategoryDto.from(category, favoriteIds))
                        .collect(Collectors.toList());
            }
        }
    }



}
