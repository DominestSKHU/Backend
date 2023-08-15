package com.dominest.dominestbackend.api.post.image.controller;

import com.dominest.dominestbackend.api.post.image.dto.SaveImageTypeDto;
import com.dominest.dominestbackend.domain.post.image.ImageTypeService;
import com.dominest.dominestbackend.domain.user.service.UserService;
import com.dominest.dominestbackend.global.util.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ImageTypeController {

    private final ImageTypeService imageTypeService;
    private final UserService userService;
    private final FileService fileService;

//    1. 제목
//  2. 작성자(user) - 외래키
//  3. url 리스트
    // 이미지 게시물 작성
    @PostMapping("/posts/image-type")
    public ResponseEntity<String> handleCreateImageType(@ModelAttribute @Valid SaveImageTypeDto.Request reqDto, Principal principal) {
        // 이미지 저장
        List<String> savedImgUrls = fileService.save(FileService.FilePrefix.POST_IMAGE_TYPE, reqDto.getPostImages());
        String email = principal.getName();
        imageTypeService.createImageType(reqDto, savedImgUrls, email);

        return ResponseEntity.ok(email);
    }
}














