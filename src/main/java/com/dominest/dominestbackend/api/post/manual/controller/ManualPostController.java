package com.dominest.dominestbackend.api.post.manual.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.post.manual.dto.CreateManualPostDto;
import com.dominest.dominestbackend.api.post.manual.dto.ManualPostListDto;
import com.dominest.dominestbackend.api.post.manual.dto.ReadManualDto;
import com.dominest.dominestbackend.api.post.manual.dto.UpdateManualPostDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.post.manual.ManualPost;
import com.dominest.dominestbackend.domain.post.manual.ManualPostService;
import com.dominest.dominestbackend.global.util.PageableUtil;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class ManualPostController {
    private final ManualPostService manualPostService;
    private final CategoryService categoryService;

    //게시글 작성
    @PostMapping("/categories/{categoryId}/posts/manual")
    public ResponseEntity<RspTemplate<Void>> handleCreateManual(
            @PathVariable Long categoryId, Principal principal, @Valid CreateManualPostDto.Req reqDto

    ) {
        String email = PrincipalUtil.toEmail(principal);

        long manualPostId = manualPostService.create(categoryId, reqDto, email);

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED,
                manualPostId + "번 게시글 작성");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspTemplate);
    }

    //게시글 목록 조회
    @GetMapping("/categories/{categoryId}/posts/manual")
    public RspTemplate<ManualPostListDto.Res> handleGetManualPostList(
            @PathVariable Long categoryId, @RequestParam(defaultValue = "1") int page) {
        final int MANUAL_TYPE_PAGE_SIZE = 20;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageableUtil.of(page, MANUAL_TYPE_PAGE_SIZE, sort);

        Category category = categoryService.validateCategoryType(categoryId, Type.MANUAL);
        Page<ManualPost> postsPage = manualPostService.getPage(category.getId(), pageable);

        ManualPostListDto.Res resDto = ManualPostListDto.Res.from(postsPage, category);
        return new RspTemplate<>(HttpStatus.OK
                , "페이지 게시글 목록 조회 - " + resDto.getPage().getCurrentPage() + "페이지"
                ,resDto);
    }

    //게시글 삭제
    @DeleteMapping("/posts/manual/{manualPostId}")
    public ResponseEntity<RspTemplate<Void>> handleDeleteManualPost(
            @PathVariable Long manualPostId
    ) {
        long deletedPostId = manualPostService.delete(manualPostId);

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK, deletedPostId + "번 게시글 삭제");
        return ResponseEntity.ok(rspTemplate);
    }

    //게시글 수정
    @PatchMapping("/posts/manual/{manualPostId}")
    public ResponseEntity<RspTemplate<Void>> handleUpdateManualPost(
            @PathVariable Long manualPostId, @Valid UpdateManualPostDto.Req reqDto
    ) {
        long updatedPostId = manualPostService.update(manualPostId, reqDto);

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK, updatedPostId + "번 게시글 수정");
        return ResponseEntity.ok(rspTemplate);
    }

    //게시글 읽기
    //categoryId와 manualId가 맞지 않는 경우에 보안 조치는 굳이 안해도 될 것 같아서 생략
    @GetMapping("/categories/{categoryId}/posts/manual/{manualId}")
    public RspTemplate<ReadManualDto.Res> handleManualPost(
            @PathVariable Long categoryId, @PathVariable Long manualId, @RequestParam(defaultValue = "1") int page) {

        ManualPost post = manualPostService.getByIdIncludeAllColumn(manualId);

        ReadManualDto.Res resDto = ReadManualDto.Res.from(post, page);
        return new RspTemplate<>(HttpStatus.OK
                , "manual 게시글 조회 - " + post.getId() + "번 게시글"
                ,resDto);

    }
}
