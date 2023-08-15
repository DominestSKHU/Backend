package com.dominest.dominestbackend.api.post.image.dto;


import com.dominest.dominestbackend.domain.post.image.ImageType;
import com.dominest.dominestbackend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class SaveImageTypeDto {
    @AllArgsConstructor
    @Getter
    public static class Request {
        @NotBlank(message = "제목은 비어있을 수 없습니다")
        private String title;
        @NotBlank(message = "내용은 비어있을 수 없습니다")
        private String content;
        @NotNull(message = "이미지는 비어있을 수 없습니다")
        private List<MultipartFile> postImages;

        public ImageType toEntity(List<String> imageUrlList, User user){
            return ImageType.builder()
                    .title(title)
                    .writer(user)
                    .imageUrls(imageUrlList)
                    .build();
        }
    }
}
