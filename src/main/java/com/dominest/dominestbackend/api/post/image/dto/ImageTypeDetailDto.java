package com.dominest.dominestbackend.api.post.image.dto;

import com.dominest.dominestbackend.domain.post.image.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ImageTypeDetailDto {
    @AllArgsConstructor
    @Getter
    public static class Res {
        ImageTypeDto postDetail;

        public static Res from(ImageType imageType) {
            ImageTypeDto imageTypeDto = ImageTypeDto.builder()
                    .createTime(imageType.getCreateTime())
                    .updateTime(imageType.getUpdateTime())
                    .title(imageType.getTitle())
                    .writer(imageType.getWriter().getName())
                    .imageUrls(imageType.getImageUrls())
                    .build();
            return new Res(imageTypeDto);
        }

        @Getter
        @Builder
        private static class ImageTypeDto {
            LocalDateTime createTime;
            LocalDateTime updateTime;
            String title;
            String writer;
            List<String> imageUrls;
        }
    }
}
