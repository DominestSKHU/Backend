package com.dominest.dominestbackend.api.post.image.dto;

import com.dominest.dominestbackend.domain.post.image.ImageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageTypeDetailDto {
    @AllArgsConstructor
    @Getter
    public static class Res {
        ImageTypeDto postDetail;

        public static Res from(ImageType imageType) {
            ImageTypeDto imageTypeDto = ImageTypeDto.builder()
                    .createTime(imageType.getCreateTime())
                    .updateTime(imageType.getLastModifiedTime())
                    .title(imageType.getTitle())
                    .writer(imageType.getWriter().getName())
                    .imageUrls(imageType.getImageUrls())
                    .build();
            return new Res(imageTypeDto);
        }

        @Getter
        @Builder
        private static class ImageTypeDto {
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
            LocalDateTime createTime;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
            LocalDateTime updateTime;
            String title;
            String writer;
            List<String> imageUrls;
        }
    }
}
