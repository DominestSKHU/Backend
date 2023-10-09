package com.dominest.dominestbackend.api.post.sanitationcheck.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.post.sanitationcheck.dto.CheckFloorListDto;
import com.dominest.dominestbackend.api.post.sanitationcheck.dto.CheckPostListDto;
import com.dominest.dominestbackend.api.post.sanitationcheck.dto.CheckedRoomListDto;
import com.dominest.dominestbackend.api.post.sanitationcheck.dto.UpdateCheckedRoomDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.SanitationCheckPost;
import com.dominest.dominestbackend.domain.post.sanitationcheck.SanitationCheckPostService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.Floor;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.FloorService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoomRepository;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoomService;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.global.util.ExcelUtil;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.security.Principal;
import java.util.List;

import static com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom.PassState.NOT_PASSED;

@RequiredArgsConstructor
@RestController
public class SanitationCheckController {
    private final SanitationCheckPostService sanitationCheckPostService;
    private final CategoryService categoryService;
    private final FloorService floorService;
    private final CheckedRoomService checkedRoomService;
    private final CheckedRoomRepository checkedRoomRepository;

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
    public RspTemplate<CheckPostListDto.Res> handleGetSanitationCheckPosts(
            @PathVariable Long categoryId, @RequestParam(defaultValue = "1") int page
    ) {
        final int IMAGE_TYPE_PAGE_SIZE = 20;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageableUtil.of(page, IMAGE_TYPE_PAGE_SIZE, sort);

        Category category = categoryService.validateCategoryType(categoryId, Type.SANITATION_CHECK);
        Page<SanitationCheckPost> postPage = sanitationCheckPostService.getPage(category.getId(), pageable);

        CheckPostListDto.Res resDto = CheckPostListDto.Res.from(postPage, category);
        return new RspTemplate<>(HttpStatus.OK
                , "페이지 게시글 목록 조회 - " + resDto.getPage().getCurrentPage() + "페이지"
                ,resDto);
    }
    // 게시글 상세조회 - 층 목록
    // posts sanitation-check num floors
    @GetMapping("/posts/sanitation-check/{postId}/floors")
    public RspTemplate<CheckFloorListDto.Res> handleGetFloors(
            @PathVariable Long postId
    ) {
        List<Floor> floors = floorService.getAllByPostId(postId);
        Category category = sanitationCheckPostService.getByIdFetchCategory(postId).getCategory();

        CheckFloorListDto.Res resDto = CheckFloorListDto.Res.from(floors, category);
        return new RspTemplate<>(HttpStatus.OK
                , postId + "번 게시글의 층 목록 조회"
                ,resDto);
    }
    // 층을 클릭해서 들어간 점검표 페이지
    // posts sanitation-check num floors num
    @GetMapping("/posts/sanitation-check/{postId}/floors/{floorId}")
    public RspTemplate<CheckedRoomListDto.Res> handleGetFloors(
            @PathVariable Long postId, @PathVariable Long floorId
    ) {
        Category category = sanitationCheckPostService.getByIdFetchCategory(postId).getCategory();
        List<CheckedRoom> checkedRooms = checkedRoomService.getAllByFloorId(floorId);

        CheckedRoomListDto.Res resDto = CheckedRoomListDto.Res.from(checkedRooms, category);
        return new RspTemplate<>(HttpStatus.OK
                , postId + "번 게시글, 층 ID: " + floorId + "의 점검표 조회"
                ,resDto);
    }

    // 미통과자
    @GetMapping("/posts/sanitation-check/{postId}/not-passed")
    public RspTemplate<CheckedRoomListDto.Res> handleGetNotPassed(
            @PathVariable Long postId
    ) {
        Category category = sanitationCheckPostService.getByIdFetchCategory(postId).getCategory();
        // 여기서 미통과자를 입사생과 함께 조회
        List<CheckedRoom> checkedRooms = checkedRoomRepository.findNotPassedAllByPostId(postId, NOT_PASSED);

        CheckedRoomListDto.Res resDto = CheckedRoomListDto.Res.from(checkedRooms, category);
        return new RspTemplate<>(HttpStatus.OK
                , postId + "번 게시글의 미통과자 목록 조회"
                ,resDto);
    }

    // 컬럼 수정
    @PatchMapping("/checked-rooms/{roomId}")
    public ResponseEntity<RspTemplate<Void>> handleUpdateCheckedRoom(
            @PathVariable Long roomId
            , @RequestBody UpdateCheckedRoomDto.Req checkedRoomDto
    ) {
        checkedRoomService.update(roomId, checkedRoomDto);
        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK, "점검표 수정 완료");
        return ResponseEntity.ok(rspTemplate);
    }

    // 전체 통과
    @PatchMapping("/checked-rooms/{roomId}/pass-all")
    public ResponseEntity<RspTemplate<Void>> handleUpdateCheckedRoomPass(
            @PathVariable Long roomId
    ) {
        checkedRoomService.passAll(roomId);
        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK, "점검표 전체 통과 완료");
        return ResponseEntity.ok(rspTemplate);
    }

    // 방호점 게시글에서 벌점이 부과된 입사생 목록
    @GetMapping("/posts/sanitation-check/{postId}/xlsx-penalty-residents")
    public void handlePenaltyCheckedRoomExcelDownload(
            @PathVariable Long postId, HttpServletResponse response
    ) {
        String postTitle = sanitationCheckPostService.getById(postId).getTitle();

        String filename = postTitle + " - 벌점 부여자 명단" + ".xlsx";
        String sheetName = "벌점 부여자 명단";

        // N차 통과를 조회하려면 CheckedRoom까지 조회해야 함. Resident를 Inner Join해서 빈 방 조회를 피하자.
        // 2~10차 통과자 목록 조회, Room 정보까지 Fetch Join함.
        List<CheckedRoom.PassState> penalty0passStates =
                List.of(NOT_PASSED, CheckedRoom.PassState.FIRST_PASSED);
        List<CheckedRoom> checkedRoomsGotPenalty = checkedRoomRepository.findAllByPostIdAndNotInPassState(postId, penalty0passStates);

        // 파일 이름 설정
        ExcelUtil.createAndRespondCheckedRoomData(filename, sheetName, response, checkedRoomsGotPenalty);
    }

    // 방호점 게시글의 특정 통과차수에 해당하는 입사생 목록
    @GetMapping("/posts/sanitation-check/{postId}/xlsx-residents")
    public void handleCheckedRoomExcelDownload(
            @PathVariable Long postId, HttpServletResponse response
            , @RequestParam(defaultValue = "NOT_PASSED") CheckedRoom.PassState passState
    ) {
        String postTitle = sanitationCheckPostService.getById(postId).getTitle();

        String passStateValue = passState.getValue();
        String filename = postTitle + " - " + passStateValue + " 명단" + ".xlsx";
        String sheetName = passStateValue + "명단";

        // N차 통과를 조회하려면 CheckedRoom까지 조회해야 함. Resident를 Inner Join해서 빈 방 조회를 피하자.
        // 2~10차 통과자 목록 조회, Room 정보까지 Fetch Join함.
        List<CheckedRoom> checkedRooms = checkedRoomRepository.findAllByPostIdAndPassState(postId, passState);

        // 파일 이름 설정
        ExcelUtil.createAndRespondCheckedRoomData(filename, sheetName, response, checkedRooms);
    }

    // 방호점 게시글의 전체 데이터 엑셀로 다운로드
    @GetMapping("/posts/sanitation-check/{postId}/xlsx-all-data")
    public void handleExcelDownloadAll(
            @PathVariable Long postId, HttpServletResponse response
    ) {
        String postTitle = sanitationCheckPostService.getById(postId).getTitle();

        String filename = postTitle + " - 점검표 전체 데이터" + ".xlsx";
        String sheetName = "점검표 전체 데이터";

        // N차 통과를 조회하려면 CheckedRoom까지 조회해야 함. Resident를 Inner Join해서 빈 방 조회를 피하자.
        List<CheckedRoom> checkedRoomsGotPenalty = checkedRoomRepository.findAllByPostId(postId);

        // 파일 이름 설정
        ExcelUtil.createAndRespondAllCheckedRoomData(filename, sheetName, response, checkedRoomsGotPenalty);
    }





}













