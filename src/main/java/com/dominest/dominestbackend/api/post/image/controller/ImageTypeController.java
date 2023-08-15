package com.dominest.dominestbackend.api.post.image.controller;

import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.api.post.image.dto.ImageTypeDetailDto;
import com.dominest.dominestbackend.api.post.image.dto.ImageTypeListDto;
import com.dominest.dominestbackend.api.post.image.dto.SaveImageTypeDto;
import com.dominest.dominestbackend.domain.post.image.ImageTypeService;
import com.dominest.dominestbackend.global.util.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ImageTypeController {

    private final ImageTypeService imageTypeService;
    private final FileService fileService;
    private final int IMAGE_TYPE_PAGE_SIZE = 6;

//    1. 제목
//  2. 작성자(user) - 외래키
//  3. url 리스트
    // 이미지 게시물 작성
    @PostMapping("/posts/image-types")
    public ResponseEntity<String> handleCreateImageType(@ModelAttribute @Valid SaveImageTypeDto.Req reqDto, Principal principal) {
        // 이미지 저장
        List<String> savedImgUrls = fileService.save(FileService.FilePrefix.POST_IMAGE_TYPE, reqDto.getPostImages());
        String email = principal.getName();
        imageTypeService.createImageType(reqDto, savedImgUrls, email);

        return ResponseEntity.ok(email);
    }
    // 이미지타입 게시물 단건 조회
    @GetMapping("/posts/image-types/{imageTypeId}")
    public ResTemplate<ImageTypeDetailDto.Res> handleGetImageType(@PathVariable Long imageTypeId) {

        ImageTypeDetailDto.Res resDto = imageTypeService.getImageTypeById(imageTypeId);
        return new ResTemplate<>(HttpStatus.OK, imageTypeId+"번 게시물  조회 성공", resDto);
    }

    // 이미지타입 게시물 목록을 조회한다. 페이지네이션 적용 필요.
    @GetMapping("/posts/image-types")
    public ResTemplate<ImageTypeListDto.Res> handleGetImageTypes(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, IMAGE_TYPE_PAGE_SIZE);
        ImageTypeListDto.Res resDto = imageTypeService.getImageTypes(pageable);
        return new ResTemplate<>(HttpStatus.OK, "게시물 목록 조회 성공,", resDto);
    }
}














