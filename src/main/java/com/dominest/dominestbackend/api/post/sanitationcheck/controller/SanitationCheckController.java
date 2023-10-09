package com.dominest.dominestbackend.api.post.sanitationcheck.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.post.sanitationcheck.dto.CheckFloorListDto;
import com.dominest.dominestbackend.api.post.sanitationcheck.dto.CheckPostListDto;
import com.dominest.dominestbackend.api.post.sanitationcheck.dto.CheckedRoomListDto;
import com.dominest.dominestbackend.api.post.sanitationcheck.dto.UpdateCheckedRoomDto;
import com.dominest.dominestbackend.api.resident.dto.ResidentListDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.SanitationCheckPost;
import com.dominest.dominestbackend.domain.post.sanitationcheck.SanitationCheckPostService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.Floor;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.FloorService;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoomService;
import com.dominest.dominestbackend.domain.resident.Resident;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.domain.room.Room;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.file.FileIOException;
import com.dominest.dominestbackend.global.util.PageableUtil;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
                , postId + "번 게시글 " + floorId + "층의 점검표 조회"
                ,resDto);
    }

    // 미통과자
    @GetMapping("/posts/sanitation-check/{postId}/not-passed")
    public RspTemplate<CheckedRoomListDto.Res> handleGetNotPassed(
            @PathVariable Long postId
    ) {
        Category category = sanitationCheckPostService.getByIdFetchCategory(postId).getCategory();
        // 여기서 미통과자를 입사생과 함께 조회
        List<CheckedRoom> checkedRooms = checkedRoomService.findNotPassedAllByPostId(postId);

        CheckedRoomListDto.Res resDto = CheckedRoomListDto.Res.from(checkedRooms, category);
        return new RspTemplate<>(HttpStatus.OK
                , postId + "번 게시글의 미통과자 목록 조회"
                ,resDto);
    }

    // 미통과자 엑셀파일로 추출
    @GetMapping("/posts/sanitation-check/{postId}/not-passed/xlsx")
    public void handleNotPassedExcelDownload(
            @PathVariable Long postId, HttpServletResponse response
    ) {
        // 미통과자 목록 조회, Room 정보까지 Fetch Join함.
        String postTitle = sanitationCheckPostService.getById(postId).getTitle();
        List<Resident> residentsNotPassed = checkedRoomService.findNotPassedResidentAllByPostId(postId);


        // 파일 이름 설정
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(postTitle + " - 미통과자 명단" +".xlsx", StandardCharsets.UTF_8)
                .build();

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

        // try-with-resources를 사용하여 워크북 생성
        try (Workbook workbook = new XSSFWorkbook()) {
            // 새로운 워크시트 생성
            Sheet sheet = workbook.createSheet("FirstSheet");
            // 헤더 행 작성
            Row headerRow = sheet.createRow(0);
            String[] headers = {"호실", "이름", "전화번호", "학번", "벌점"};

            for (int i=0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 전화번호, 학번 컬럼은 width를 autoSize 설정
            int columnWidth14 = 14 * 256; // 14문자 너비
            int columnWidth10 = 10 * 256; // 10문자 너비
            sheet.setColumnWidth(2, columnWidth14);
            sheet.setColumnWidth(3, columnWidth10);

            // 데이터 작성
            for (int rowNum = 1; rowNum <= residentsNotPassed.size(); rowNum++) {
                Row row = sheet.createRow(rowNum);

                Resident resident = residentsNotPassed.get(rowNum - 1);
                Room room = resident.getRoom();
                String assignedRoom = room != null ? room.getAssignedRoom() : "";


                row.createCell(0).setCellValue(assignedRoom);
                row.createCell(1).setCellValue(resident.getName());
                row.createCell(2).setCellValue(resident.getPhoneNumber());
                row.createCell(3).setCellValue(resident.getStudentId());
                row.createCell(4).setCellValue(resident.getPenalty());
            }

            // 파일 내보내기
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_SENT);
        }
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





}













