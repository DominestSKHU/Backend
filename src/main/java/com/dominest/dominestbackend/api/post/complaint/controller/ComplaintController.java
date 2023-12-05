package com.dominest.dominestbackend.api.post.complaint.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.post.complaint.dto.ComplaintListDto;
import com.dominest.dominestbackend.api.post.complaint.dto.CreateComplaintDto;
import com.dominest.dominestbackend.api.post.complaint.dto.UpdateComplaintDto;
import com.dominest.dominestbackend.domain.post.complaint.Complaint;
import com.dominest.dominestbackend.domain.post.complaint.ComplaintRepository;
import com.dominest.dominestbackend.domain.post.complaint.ComplaintService;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.global.util.ExcelUtil;
import com.dominest.dominestbackend.global.util.FileService;
import com.dominest.dominestbackend.global.util.PageableUtil;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ComplaintController {
    private final ComplaintService complaintService;
    private final CategoryService categoryService;
    private final ComplaintRepository complaintRepository;

    // 민원 등록
    @PostMapping("/categories/{categoryId}/posts/complaint")
    public ResponseEntity<RspTemplate<Void>> handleCreateComplaint(
            @RequestBody @Valid CreateComplaintDto.Req reqDto
            , @PathVariable Long categoryId, Principal principal
    ) {
        String email = PrincipalUtil.toEmail(principal);
        long complaintId = complaintService.create(reqDto, categoryId, email);
        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED, complaintId + "번 민원 작성");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rspTemplate);
    }

    // 민원 수정
    @PatchMapping("/complaints/{complaintId}")
    public RspTemplate<Void> handleUpdateComplaint(
            @PathVariable Long complaintId, @RequestBody @Valid UpdateComplaintDto.Req reqDto
    ) {
        // parcelId 조회, 값 바꿔치기, 저장하기
        long updatedId = complaintService.update(complaintId, reqDto);

        return new RspTemplate<>(HttpStatus.OK, updatedId + "번 민원내역 수정");
    }

    // 민원 삭제
    @DeleteMapping("/complaints/{complaintId}")
    public RspTemplate<Void> handleDeleteComplaint(
            @PathVariable Long complaintId
    ) {
        long deleteId = complaintService.delete(complaintId);

        return new RspTemplate<>(HttpStatus.OK, deleteId + "번 민원내역 삭제");
    }

    // 민원 목록 조회. 최신등록순
    @GetMapping("/categories/{categoryId}/posts/complaint")
    public RspTemplate<ComplaintListDto.Res> handleGetComplaints(
            @PathVariable Long categoryId, @RequestParam(defaultValue = "1") int page
            , @RequestParam(required = false) String roomNoSch
            , @RequestParam(required = false) String complSchText
    ) {
        final int COMPLAINT_TYPE_PAGE_SIZE = 20;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageableUtil.of(page, COMPLAINT_TYPE_PAGE_SIZE, sort);

        Category category = categoryService.validateCategoryType(categoryId, Type.COMPLAINT);

        Page<Complaint> complaintPage = complaintService.getPage(category.getId(), pageable, complSchText, roomNoSch);

        ComplaintListDto.Res resDto = ComplaintListDto.Res.from(complaintPage, category);
        return new RspTemplate<>(HttpStatus.OK
                , "(생성일자 내림차순) 페이지  목록 조회 - " + resDto.getPage().getCurrentPage() + "페이지"
                ,resDto);
    }

    @GetMapping("/categories/{categoryId}/posts/complaint/xlsx")
    public void handleGetComplaintsXlsx(
            @PathVariable Long categoryId
            , @RequestParam(required = false) Integer downloadCnt
            , HttpServletResponse response
    ) {
        Category category = categoryService.validateCategoryType(categoryId, Type.COMPLAINT);

        List<Complaint> complaints;
        long complaintCnt = complaintRepository.countByCategoryId(category.getId());

        String filename;
        LocalDate now = LocalDate.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
        StringBuilder sb = new StringBuilder();
        if (downloadCnt == null) {
            complaints = complaintRepository.findAllByCategoryId(category.getId(), Sort.by(Order.desc("id")));
            filename = sb.append(formattedDate)
                    .append(" 민원접수내역 전체 ")
                    .append(complaintCnt).append("건")
                    .append(".").append(FileService.FileExt.XLSX.value)
                    .toString();
        } else {
            complaints = complaintRepository.findAllByCategoryId(
                    category.getId(),
                    PageableUtil.of(1, downloadCnt)
            );
            filename = sb.append(formattedDate)
                    .append(" 민원접수내역 최신 ")
                    .append(downloadCnt).append("건")
                    .append(".").append(FileService.FileExt.XLSX.value)
                    .toString();
        }
        String sheetName = "민원접수내역";

        ExcelUtil.createAndRespondAllDataWitehComplaint(filename, sheetName, response, complaints);
    }

}

















