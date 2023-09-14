package com.dominest.dominestbackend.api.post.sanitationcheck.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.post.sanitationcheck.dto.SaniCheckFloorListDto;
import com.dominest.dominestbackend.api.post.sanitationcheck.dto.SaniCheckPostListDto;
import com.dominest.dominestbackend.api.post.sanitationcheck.dto.SaniCheckedRoomListDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.SanitationCheckPost;
import com.dominest.dominestbackend.domain.post.sanitationcheck.SanitationCheckPostService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.Floor;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.FloorService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoomService;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.global.util.PageableUtil;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class SanitationCheckController {
    private final SanitationCheckPostService sanitationCheckPostService;
    private final CategoryService categoryService;
    private final FloorService floorService;
    private final CheckedRoomService checkedRoomService;

    // 게시글 생성(학기 지정)
    // category 4 posts sanitation-check
    @PostMapping("/categories/{categoryId}/posts/sanitation-check")
    public ResponseEntity<RspTemplate<Void>> handleCreateSanitationCheckPost(
            @PathVariable Long categoryId, Principal principal
            , @RequestBody @Valid ResidenceSemesterDto residenceSemesterDto
    ) {
        String email = PrincipalUtil.toEmail(principal);

        long saniChkPostId = sanitationCheckPostService.create(
                residenceSemesterDto.getResidenceSemester()
                , categoryId, email);

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED, saniChkPostId + "번 게시글 작성");
        return ResponseEntity
                .created(URI.create("/categories/"+categoryId+"/posts/sanitation-check/" + saniChkPostId))
                .body(rspTemplate);
    }
    @Getter
    @NoArgsConstructor
    public static class ResidenceSemesterDto {
        @NotNull(message = "학기를 선택해주세요.")
        ResidenceSemester residenceSemester;
    }


    // 게시글 제목 수정
    @PatchMapping("/posts/sanitation-check/{postId}")
    public ResponseEntity<RspTemplate<Void>> handleUpdateSanitationCheckPostTitle(
            @PathVariable Long postId, @RequestBody @Valid TitleDto titleDto
    ) {
        long updatedPostId = sanitationCheckPostService.updateTitle(postId, titleDto.getTitle());

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK, updatedPostId + "번 게시글 제목 수정");
        return ResponseEntity.ok(rspTemplate);
    }
    @Getter
    @NoArgsConstructor
    public static class TitleDto {
        @NotBlank(message = "제목을 입력해주세요.")
        String title;
    }

    // 게시글 목록
    // category 4 posts sanitation-check
    @GetMapping("/categories/{categoryId}/posts/sanitation-check")
    public RspTemplate<SaniCheckPostListDto.Res> handleGetSanitationCheckPosts(
            @PathVariable Long categoryId, @RequestParam(defaultValue = "1") int page
    ) {
        final int IMAGE_TYPE_PAGE_SIZE = 20;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageableUtil.of(page, IMAGE_TYPE_PAGE_SIZE, sort);

        Category category = categoryService.validateCategoryType(categoryId, Type.SANITATION_CHECK);
        Page<SanitationCheckPost> postPage = sanitationCheckPostService.getPage(category.getId(), pageable);

        SaniCheckPostListDto.Res resDto = SaniCheckPostListDto.Res.from(postPage, category);
        return new RspTemplate<>(HttpStatus.OK
                , "페이지 게시글 목록 조회 - " + resDto.getPage().getCurrentPage() + "페이지"
                ,resDto);
    }
    // 게시글 상세조회 - 층 목록
    // posts sanitation-check num floors
    @GetMapping("/posts/sanitation-check/{postId}/floors")
    public RspTemplate<SaniCheckFloorListDto.Res> handleGetFloors(
            @PathVariable Long postId
    ) {
        List<Floor> floors = floorService.getAllByPostId(postId);
        Category category = sanitationCheckPostService.getByIdFetchCategory(postId).getCategory();

        SaniCheckFloorListDto.Res resDto = SaniCheckFloorListDto.Res.from(floors, category);
        return new RspTemplate<>(HttpStatus.OK
                , postId + "번 게시글의 층 목록 조회"
                ,resDto);
    }
    // 층을 클릭해서 들어간 점검표 페이지
    // posts sanitation-check num floors num
    @GetMapping("/posts/sanitation-check/{postId}/floors/{floorId}")
    public RspTemplate<SaniCheckedRoomListDto.Res> handleGetFloors(
            @PathVariable Long postId, @PathVariable Long floorId
    ) {
        Category category = sanitationCheckPostService.getByIdFetchCategory(postId).getCategory();
        List<CheckedRoom> checkedRooms = checkedRoomService.getAllByFloorId(floorId);

        SaniCheckedRoomListDto.Res resDto = SaniCheckedRoomListDto.Res.from(checkedRooms, category);
        return new RspTemplate<>(HttpStatus.OK
                , postId + "번 게시글 " + floorId + "층의 점검표 조회"
                ,resDto);
    }
}













