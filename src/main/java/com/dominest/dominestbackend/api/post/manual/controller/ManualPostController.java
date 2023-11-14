package com.dominest.dominestbackend.api.post.manual.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.post.manual.dto.CreateManualPostDto;
import com.dominest.dominestbackend.domain.post.manual.ManualPostService;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class ManualPostController {
    private final ManualPostService manualPostService;
    @PostMapping("/categories/{categoryId}/posts/manual")
    public ResponseEntity<RspTemplate<Void>> handleCreateManual(
            @PathVariable Long categoryId, Principal principal, @RequestBody @Valid CreateManualPostDto.Req reqDto
    ) {
        String email = PrincipalUtil.toEmail(principal);

        long manualPostId = manualPostService.create(categoryId, reqDto, email);

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED,
                manualPostId + "번 게시글 작성");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspTemplate);
    }
}
