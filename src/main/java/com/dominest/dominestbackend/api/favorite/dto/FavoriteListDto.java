package com.dominest.dominestbackend.api.favorite.dto;

import com.dominest.dominestbackend.domain.favorite.Favorite;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteListDto {
    @Getter
    public static class Res{
        List<FavoriteDto> favorites;

        public static Res from(List<Favorite> favorites) {
            List<FavoriteDto> favoriteDtos = FavoriteDto.from(favorites);
            return new Res(favoriteDtos);
        }

        Res(List<FavoriteDto> favorites) {
            this.favorites = favorites;
        }

        @Builder
        @Getter
        // 즐겨찾기 ID, 카테고리 이름, 카테고리 링크
        private static class FavoriteDto {
            long id;
            String categoryName;
            String categoryLink;
            long categoryId;

            static FavoriteDto from(Favorite favorite) {
                Category category = favorite.getCategory();
                return FavoriteDto.builder()
                        .id(favorite.getId())
                        .categoryName(category.getName())
                        .categoryLink(category.getPostsLink())
                        .categoryId(category.getId())
                        .build();
            }

            static List<FavoriteDto> from(List<Favorite> favorites) {
                return favorites.stream()
                        .map(FavoriteDto::from)
                        .collect(Collectors.toList());
            }
        }
    }
}
