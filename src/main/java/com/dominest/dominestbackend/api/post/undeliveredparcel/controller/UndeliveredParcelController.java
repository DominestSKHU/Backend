package com.dominest.dominestbackend.api.post.undeliveredparcel.controller;

import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.api.post.undeliveredparcel.dto.CreateUndelivParcelDto;
import com.dominest.dominestbackend.api.post.undeliveredparcel.dto.UndelivParcelPostDetailDto;
import com.dominest.dominestbackend.api.post.undeliveredparcel.dto.UndelivParcelPostListDto;
import com.dominest.dominestbackend.api.post.undeliveredparcel.dto.UpdateUndelivParcelDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.UndeliveredParcelPost;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.UndeliveredParcelPostService;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.component.UndeliveredParcelService;
import com.dominest.dominestbackend.global.util.PageableUtil;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class UndeliveredParcelController {
    private final UndeliveredParcelPostService undelivParcelPostService;
    private final UndeliveredParcelService undeliveredParcelService;
    private final CategoryService categoryService;

    // 게시글 등록
    @PostMapping("/categories/{categoryId}/posts/undelivered-parcel")
    public ResponseEntity<ResTemplate<Void>> handleCreateParcelPost(
            @PathVariable Long categoryId, Principal principal
    ) {
        // 이미지 저장
        String email = PrincipalUtil.toEmail(principal);
        long unDeliParcelId = undelivParcelPostService.create(categoryId, email);
        ResTemplate<Void> resTemplate = new ResTemplate<>(HttpStatus.CREATED, unDeliParcelId + "번 게시글 작성");

        return ResponseEntity
                .created(URI.create("/categories/"+categoryId+"/posts/undelivered-parcel/" + unDeliParcelId))
                .body(resTemplate);
    }

    //  게시글 목록 조회
    @GetMapping("/categories/{categoryId}/posts/undelivered-parcel")
    public ResTemplate<UndelivParcelPostListDto.Res> handleGetParcelPosts(
            @PathVariable Long categoryId, @RequestParam(defaultValue = "1") int page
    ) {
        final int IMAGE_TYPE_PAGE_SIZE = 20;
        Pageable pageable = PageableUtil.of(page, IMAGE_TYPE_PAGE_SIZE);

        Category category = categoryService.validateCategoryType(categoryId, Type.UNDELIVERED_PARCEL_REGISTER);
        Page<UndeliveredParcelPost> postsPage = undelivParcelPostService.getPage(categoryId, pageable);

        UndelivParcelPostListDto.Res resDto = UndelivParcelPostListDto.Res.from(postsPage, category);
        return new ResTemplate<>(HttpStatus.OK
                , "페이지 게시글 목록 조회 - " + resDto.getPage().getCurrentPage() + "페이지"
                ,resDto);
    }

    // 게시글 삭제
    @DeleteMapping("/categories/{categoryId}/posts/undelivered-parcel/{undelivParcelPostId}")
    public ResponseEntity<ResTemplate<Void>> handleDeleteParcelPost(
            @PathVariable Long categoryId, @PathVariable Long undelivParcelPostId
    ) {
        long deletedPostId = undelivParcelPostService.delete(undelivParcelPostId);

        ResTemplate<Void> resTemplate = new ResTemplate<>(HttpStatus.OK, deletedPostId + "번 게시글 삭제");
        return ResponseEntity.ok(resTemplate);
    }

    // 게시글 내부 관리목록에 관리물품 등록
    @PostMapping("/categories/{categoryId}/posts/undelivered-parcel/{undelivParcelPostId}")
    public ResponseEntity<ResTemplate<Void>> handleCreateParcel(
            @PathVariable Long categoryId, @PathVariable Long undelivParcelPostId
            , @RequestBody CreateUndelivParcelDto.Req reqDto
            ) {
        Long undelivParcelId = undeliveredParcelService.create(undelivParcelPostId, reqDto);

        ResTemplate<Void> resTemplate = new ResTemplate<>(HttpStatus.CREATED,
                "관리대장에 " + undelivParcelId + "번 관리물품 작성");
        return ResponseEntity.status(HttpStatus.CREATED).body(resTemplate);
    }

    // 게시글 상세 조회
    @GetMapping("/categories/{categoryId}/posts/undelivered-parcel/{undelivParcelPostId}")
    public ResTemplate<UndelivParcelPostDetailDto.Res> handleGetParcels(
            @PathVariable Long categoryId, @PathVariable Long undelivParcelPostId
    ) {
        UndeliveredParcelPost undelivParcelPost = undelivParcelPostService.getByIdFetchParcels(undelivParcelPostId);

        UndelivParcelPostDetailDto.Res resDto = UndelivParcelPostDetailDto.Res.from(undelivParcelPost);
        return new ResTemplate<>(HttpStatus.OK, "택배 관리대장 게시물 상세조회", resDto);
    }

    // 관리물품 단건 수정
    @PatchMapping("/undelivParcels/{undelivParcelId}")
    public ResTemplate<Void> handleUpdateParcel(
            @PathVariable Long undelivParcelId, @RequestBody UpdateUndelivParcelDto.Req reqDto
    ) {
        // parcelId 조회, 값 바꿔치기, 저장하기
        long updatedId = undeliveredParcelService.update(undelivParcelId, reqDto);

        return new ResTemplate<>(HttpStatus.OK, updatedId + "번 관리물품 수정");
    }

    // 관리물품 단건 삭제
    @PatchMapping("/undelivParcels/{undelivParcelId}")
    public ResTemplate<Void> handleDeleteParcel(
            @PathVariable Long undelivParcelId
    ) {
        // parcelId 조회, 값 바꿔치기, 저장하기
        long deleteId = undeliveredParcelService.delete(undelivParcelId);

        return new ResTemplate<>(HttpStatus.OK, deleteId + "번 관리물품 삭제");
    }

}















