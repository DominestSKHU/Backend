package com.dominest.dominestbackend.api.post.manual.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.post.manual.dto.CreateManualPostDto;
import com.dominest.dominestbackend.api.post.manual.dto.ManualPostListDto;
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
    public RspTemplate<ManualPostListDto.Res> handleGetManual(
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
}
