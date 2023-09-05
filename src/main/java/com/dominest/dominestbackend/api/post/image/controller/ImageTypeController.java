package com.dominest.dominestbackend.api.post.image.controller;

import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.api.post.image.dto.ImageTypeDetailDto;
import com.dominest.dominestbackend.api.post.image.dto.ImageTypeListDto;
import com.dominest.dominestbackend.api.post.image.dto.SaveImageTypeDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.post.image.ImageType;
import com.dominest.dominestbackend.domain.post.image.ImageTypeService;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.file.FileIOException;
import com.dominest.dominestbackend.global.util.FileService;
import com.dominest.dominestbackend.global.util.PageableUtil;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final CategoryService categoryService;

    //    1. 제목
    //  2. 작성자(user) - 외래키
    //  3. url 리스트
    // 이미지 게시물 작성
    @PostMapping("/categories/{categoryId}/posts/image-types")
    public ResponseEntity<ResTemplate<Void>> handleCreateImageType(@Valid SaveImageTypeDto.Req reqDto
                                                                                , @PathVariable Long categoryId, Principal principal) {
        // 이미지 저장
        String email = PrincipalUtil.toEmail(principal);
        long imageTypeId = imageTypeService.create(reqDto, categoryId, email);
        ResTemplate<Void> resTemplate = new ResTemplate<>(HttpStatus.CREATED, imageTypeId + "번 게시글 작성");

        return ResponseEntity
                .created(URI.create("/posts/image-types/" + imageTypeId))
                .body(resTemplate);
    }

    /**
     *  게시글 수정
     *  원본 게시글 데이터를 받아서 업데이트.
     *  최초 생성자 이름은 유지하지만, 수정 시 권한을 체크하지 않고 수정자 이름만 변경한다.
     */
    @PatchMapping("/posts/image-types/{imageTypeId}")
    public ResTemplate<Void> handleUpdateImageType(@PathVariable Long imageTypeId
                                                                                        , @Valid SaveImageTypeDto.Req reqDto) {
        long updatedImageTypeId = imageTypeService.update(reqDto, imageTypeId);
        return new ResTemplate<>(HttpStatus.OK, updatedImageTypeId + "번 게시글 수정");
    }

    /**
     *  게시글 삭제. 권한을 체크하지 않는다.
     */
    @DeleteMapping("/posts/image-types/{imageTypeId}")
    public ResTemplate<Void> handleUpdateImageType(@PathVariable Long imageTypeId) {
        ImageType imageType = imageTypeService.deleteById(imageTypeId);

        List<String> imageUrlsToDelete = imageType.getImageUrls();
        fileService.deleteFile(FileService.FilePrefix.POST_IMAGE_TYPE, imageUrlsToDelete);

        return new ResTemplate<>(HttpStatus.OK, imageType.getId() + "번 게시글 삭제");
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
    @GetMapping("/categories/{categoryId}/posts/image-types/{imageTypeId}")
    public ResTemplate<ImageTypeDetailDto.Res> handleGetImageType(
            @PathVariable Long categoryId, @PathVariable Long imageTypeId
    ) {
        ImageType imageType = imageTypeService.getById(imageTypeId);

        ImageTypeDetailDto.Res resDto = ImageTypeDetailDto.Res.from(imageType);
        return new ResTemplate<>(HttpStatus.OK
                , imageTypeId+"번 게시물  조회 성공"
                , resDto);
    }

    // 게시물 목록을 조회한다.
    @GetMapping("/categories/{categoryId}/posts/image-types")
    public ResTemplate<ImageTypeListDto.Res> handleGetImageTypes(@PathVariable Long categoryId, @RequestParam(defaultValue = "1") int page) {
        final int IMAGE_TYPE_PAGE_SIZE = 20;
        Pageable pageable = PageableUtil.of(page, IMAGE_TYPE_PAGE_SIZE);

        Category category = categoryService.validateCategoryType(categoryId, Type.IMAGE);
        // 카테고리 내 게시글이 1건도 없는 경우도 있으므로, 게시글과 함께 카테고리를 Join해서 데이터를 찾아오지 않는다.
        Page<ImageType> imageTypes = imageTypeService.getPage(categoryId, pageable);

        ImageTypeListDto.Res resDto = ImageTypeListDto.Res.from(imageTypes, category);
        return new ResTemplate<>(HttpStatus.OK
                , "페이지 게시글 목록 조회 - " + resDto.getPage().getCurrentPage() + "페이지"
                , resDto);
    }
}














