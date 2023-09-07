package com.dominest.dominestbackend.api.post.undeliveredparcel.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
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
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<RspTemplate<Void>> handleCreateParcelPost(
            @PathVariable Long categoryId, Principal principal
    ) {
        String email = PrincipalUtil.toEmail(principal);
        long unDeliParcelId = undelivParcelPostService.create(categoryId, email);
        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED, unDeliParcelId + "번 게시글 작성");

        return ResponseEntity
                .created(URI.create("/categories/"+categoryId+"/posts/undelivered-parcel/" + unDeliParcelId))
                .body(rspTemplate);
    }

    //  게시글 목록 조회
    @GetMapping("/categories/{categoryId}/posts/undelivered-parcel")
    public RspTemplate<UndelivParcelPostListDto.Res> handleGetParcelPosts(
            @PathVariable Long categoryId, @RequestParam(defaultValue = "1") int page
    ) {
        final int IMAGE_TYPE_PAGE_SIZE = 20;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageableUtil.of(page, IMAGE_TYPE_PAGE_SIZE, sort);

        Category category = categoryService.validateCategoryType(categoryId, Type.UNDELIVERED_PARCEL_REGISTER);
        Page<UndeliveredParcelPost> postsPage = undelivParcelPostService.getPage(category.getId(), pageable);

        UndelivParcelPostListDto.Res resDto = UndelivParcelPostListDto.Res.from(postsPage, category);
        return new RspTemplate<>(HttpStatus.OK
                , "페이지 게시글 목록 조회 - " + resDto.getPage().getCurrentPage() + "페이지"
                ,resDto);
    }

    // 게시글 삭제
    @DeleteMapping("/posts/undelivered-parcel/{undelivParcelPostId}")
    public ResponseEntity<RspTemplate<Void>> handleDeleteParcelPost(
            @PathVariable Long undelivParcelPostId
    ) {
        long deletedPostId = undelivParcelPostService.delete(undelivParcelPostId);

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK, deletedPostId + "번 게시글 삭제");
        return ResponseEntity.ok(rspTemplate);
    }

    // 게시글 내부 관리목록에 관리물품 등록
    @PostMapping("/posts/undelivered-parcel/{undelivParcelPostId}")
    public ResponseEntity<RspTemplate<Void>> handleCreateParcel(
                @PathVariable Long undelivParcelPostId, @RequestBody CreateUndelivParcelDto.Req reqDto
            ) {
        Long undelivParcelId = undeliveredParcelService.create(undelivParcelPostId, reqDto);

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED,
                undelivParcelPostId + "번 관리대장 게시글에" +  undelivParcelId + "번 관리물품 작성");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspTemplate);
    }

    // 게시글 상세 조회
    @GetMapping("/posts/undelivered-parcel/{undelivParcelPostId}")
    public RspTemplate<UndelivParcelPostDetailDto.Res> handleGetParcels(
            @PathVariable Long undelivParcelPostId
    ) {
        UndeliveredParcelPost undelivParcelPost = undelivParcelPostService.getByIdFetchParcels(undelivParcelPostId);

        UndelivParcelPostDetailDto.Res resDto = UndelivParcelPostDetailDto.Res.from(undelivParcelPost);
        return new RspTemplate<>(HttpStatus.OK, "택배 관리대장 게시물 상세조회", resDto);
    }

    // 관리물품 단건 수정
    @PatchMapping("/undeliv-parcels/{undelivParcelId}")
    public RspTemplate<Void> handleUpdateParcel(
            @PathVariable Long undelivParcelId, @RequestBody UpdateUndelivParcelDto.Req reqDto
    ) {
        // parcelId 조회, 값 바꿔치기, 저장하기
        long updatedId = undeliveredParcelService.update(undelivParcelId, reqDto);

        return new RspTemplate<>(HttpStatus.OK, updatedId + "번 관리물품 수정");
    }

    // 관리물품 단건 삭제
    @DeleteMapping("/undeliv-parcels/{undelivParcelId}")
    public RspTemplate<Void> handleDeleteParcel(
            @PathVariable Long undelivParcelId
    ) {
        long deleteId = undeliveredParcelService.delete(undelivParcelId);

        return new RspTemplate<>(HttpStatus.OK, deleteId + "번 관리물품 삭제");
    }

}















