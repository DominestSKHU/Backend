package com.dominest.dominestbackend.api.post.complaint.controller;

import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.api.post.complaint.dto.CreateComplaintDto;
import com.dominest.dominestbackend.domain.post.complaint.ComplaintService;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class ComplaintController {
    private final ComplaintService complaintService;
    private final CategoryService categoryService;

    // 민원 등록
    @PostMapping("/categories/{categoryId}/posts/complaint")
    public ResponseEntity<ResTemplate<Void>> handleCreateComplaint(
            @RequestBody CreateComplaintDto.Req reqDto
            , @PathVariable Long categoryId, Principal principal
    ) {
        String email = PrincipalUtil.toEmail(principal);
        long complaintId = complaintService.create(reqDto, categoryId, email);
        ResTemplate<Void> resTemplate = new ResTemplate<>(HttpStatus.CREATED, complaintId + "번 민원 작성");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resTemplate);
    }
}
