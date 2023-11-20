package com.dominest.dominestbackend.api.post.manual.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateManualPostDto {

    @AllArgsConstructor
    @Getter
    public static class Req {

        @Length(max = 30, message = "제목은 30자를 넘을 수 없습니다")
        String title = "";

        String htmlContent = ""; // html형식의 게시글 내용

        //첨부파일
        List<MultipartFile> imageFiles;
        List<MultipartFile> videoFiles;
        List<MultipartFile> attachFiles;

        //삭제될 파일 목록
        List<String> toDeleteImageUrls;
        List<String> toDeleteVideoUrls;
        List<String> toDeleteAttachUrls;
    }
}
