package com.dominest.dominestbackend.api.post.manual.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateManualPostDto {
    @AllArgsConstructor
    @Getter
    public static class Req {

        @NotBlank(message = "제목은 비어있을 수 없습니다")
        @Length(max = 30, message = "제목은 30자를 넘을 수 없습니다")
        String title;

        String htmlContent; // html형식의 게시글 내용

        //첨부파일
        Set<MultipartFile> imageFiles;
        Set<MultipartFile> videoFiles;
        Set<MultipartFile> attachFiles;
    }
}
