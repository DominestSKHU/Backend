package com.dominest.dominestbackend.api.post.image.controller;

import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.api.post.image.dto.ImageTypeDetailDto;
import com.dominest.dominestbackend.api.post.image.dto.ImageTypeListDto;
import com.dominest.dominestbackend.api.post.image.dto.SaveImageTypeDto;
import com.dominest.dominestbackend.domain.post.image.ImageType;
import com.dominest.dominestbackend.domain.post.image.ImageTypeService;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.file.FileIOException;
import com.dominest.dominestbackend.global.util.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
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
    public ResponseEntity<ResTemplate<?>> handleCreateImageType(@ModelAttribute @Valid SaveImageTypeDto.Req reqDto, Principal principal) {
        // 이미지 저장
        List<String> savedImgUrls = fileService.save(FileService.FilePrefix.POST_IMAGE_TYPE, reqDto.getPostImages());
        String email = principal.getName();
        ImageType imageType = imageTypeService.createImageType(reqDto, savedImgUrls, email);
        ResTemplate<?> resTemplate = new ResTemplate<>(HttpStatus.CREATED, imageType.getId() + "번 게시글 작성");

        return ResponseEntity.
                created(URI.create("/posts/image-types/" + imageType.getId()))
                .body(resTemplate);
    }

    // 게시물 이미지 조회
    @GetMapping("/posts/image-types/images")
    public void getImage(HttpServletResponse response, @RequestParam(required = true) String filename) {
        byte[] bytes = fileService.getByteArr(FileService.FilePrefix.POST_IMAGE_TYPE, filename);

        response.setContentType("image/*");
        try {
            response.getOutputStream().write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_SENT);
        }
    }

    // 게시물 단건 조회
    @GetMapping("/posts/image-types/{imageTypeId}")
    public ResTemplate<ImageTypeDetailDto.Res> handleGetImageType(@PathVariable Long imageTypeId) {

        ImageTypeDetailDto.Res resDto = imageTypeService.getImageTypeById(imageTypeId);
        return new ResTemplate<>(HttpStatus.OK
                , imageTypeId+"번 게시물  조회 성공"
                , resDto);
    }

    // 게시물 목록을 조회한다.
    @GetMapping("/posts/image-types")
    public ResTemplate<ImageTypeListDto.Res> handleGetImageTypes(@RequestParam(defaultValue = "1") int page) {
        if (page < 1)
            throw new IllegalArgumentException("page는 1 이상이어야 합니다.");

        // 0-base인 페이지를 클라이언트단에서 1-based인 것처럼 사용할 수 있게 함
        Pageable pageable = PageRequest.of(page - 1 , IMAGE_TYPE_PAGE_SIZE);
        ImageTypeListDto.Res resDto = imageTypeService.getImageTypes(pageable);
        return new ResTemplate<>(HttpStatus.OK
                , "페이지 게시글 목록 조회 - " + resDto.getPage().getCurrentPage() + "페이지"
                , resDto);
    }
}














