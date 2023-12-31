package com.dominest.dominestbackend.api.post.image.dto;


import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.image.ImageType;
import com.dominest.dominestbackend.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SaveImageTypeDto {
    @AllArgsConstructor
    @Getter
    public static class Req {
        @NotBlank(message = "제목은 비어있을 수 없습니다")
        String title;
        @NotNull(message = "이미지는 비어있을 수 없습니다")
        List<MultipartFile> postImages;

        public ImageType toEntity(List<String> imageUrlList, User writer, Category category){
            return ImageType.builder()
                    .title(title)
                    .writer(writer)
                    .category(category)
                    .imageUrls(imageUrlList)
                    .build();
        }
    }
}
