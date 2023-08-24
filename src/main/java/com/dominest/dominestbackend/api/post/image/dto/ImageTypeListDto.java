package com.dominest.dominestbackend.api.post.image.dto;

import com.dominest.dominestbackend.api.common.PageInfoDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.image.ImageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ImageTypeListDto {

    @Getter
    public static class Res {
        PageInfoDto page; // 페이징 정보
        List<ImageTypeDto> posts; // 게시글 목록
        CategoryDto category; // 카테고리 정보

        public static Res from(Page<ImageType> imageTypes, Category category){
            CategoryDto categoryDto = CategoryDto.from(category);
            PageInfoDto pageInfoDto = PageInfoDto.from(imageTypes);
            List<ImageTypeDto> imageTypeDtos = ImageTypeDto.from(imageTypes);

            return new Res(pageInfoDto, imageTypeDtos, categoryDto);
        }

        Res(PageInfoDto page, List<ImageTypeDto> posts, CategoryDto category) {
            this.page = page;
            this.posts = posts;
            this.category = category;
        }

        @Getter
        @Builder
        private static class ImageTypeDto {
            long id;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
            LocalDateTime createTime;
            String title;
            String writer;

            static ImageTypeDto from(ImageType imageType){
                return ImageTypeDto.builder()
                        .id(imageType.getId())
                        .createTime(imageType.getCreateTime())
                        .title(imageType.getTitle())
                        .writer(imageType.getWriter().getName())
                        .build();
            }

            static List<ImageTypeDto> from(Page<ImageType> imageTypes){
                return imageTypes.stream()
                        .map(ImageTypeDto::from)
                        .collect(Collectors.toList());
            }
        }

        @Getter
        @Builder
        private static class CategoryDto {
            long id;
            String categoryName;
            Type type;

            static CategoryDto from(Category category){
                return CategoryDto.builder()
                        .id(category.getId())
                        .categoryName(category.getName())
                        .type(category.getType())
                        .build();
            }
        }
    }

}
