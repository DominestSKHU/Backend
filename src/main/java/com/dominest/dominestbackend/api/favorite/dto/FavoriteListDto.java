package com.dominest.dominestbackend.api.favorite.dto;

import com.dominest.dominestbackend.domain.favorite.Favorite;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteListDto {
    @Getter
    public static class Res{
        List<FavoriteDto> favorites;

        public static Res from(List<Favorite> favorites) {
            return new Res(FavoriteDto.from(favorites));
        }

        public Res(List<FavoriteDto> favorites) {
            this.favorites = favorites;
        }

        @Builder
        @Getter
        // 즐겨찾기 ID, 카테고리 이름, 카테고리 링크
        private static class FavoriteDto {
            Long id;
            String categoryName;
            String categoryLink;

            static FavoriteDto from(Favorite favorite) {
                return FavoriteDto.builder()
                        .id(favorite.getId())
                        .categoryName(favorite.getCategory().getName())
                        .categoryLink(favorite.getCategory().getPostsLink())
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
