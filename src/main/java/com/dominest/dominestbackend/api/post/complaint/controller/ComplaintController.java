package com.dominest.dominestbackend.api.post.complaint.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.post.complaint.dto.CreateComplaintDto;
import com.dominest.dominestbackend.api.post.complaint.dto.UpdateComplaintDto;
import com.dominest.dominestbackend.domain.post.complaint.ComplaintService;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class ComplaintController {
    private final ComplaintService complaintService;
    private final CategoryService categoryService;

    // 민원 등록
    @PostMapping("/categories/{categoryId}/posts/complaint")
    public ResponseEntity<RspTemplate<Void>> handleCreateComplaint(
            @RequestBody CreateComplaintDto.Req reqDto
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
            @PathVariable Long complaintId, @RequestBody UpdateComplaintDto.Req reqDto
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
}

















