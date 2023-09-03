package com.dominest.dominestbackend.api.post.image.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateImageTypeDto {
    @AllArgsConstructor
    @Getter
    public static class Req {
        @NotBlank(message = "제목은 비어있을 수 없습니다")
        String title;
        @NotNull(message = "이미지는 비어있을 수 없습니다")
        List<MultipartFile> postImages;
    }
}
