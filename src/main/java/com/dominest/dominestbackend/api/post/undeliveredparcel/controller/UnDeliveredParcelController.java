package com.dominest.dominestbackend.api.post.undeliveredparcel.controller;

import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.api.post.undeliveredparcel.dto.CreateUndelivParcelDto;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.UndeliveredParcelPostService;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.component.UndeliveredParcelService;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class UnDeliveredParcelController {
    private final UndeliveredParcelPostService undelivParcelPostService;
    private final UndeliveredParcelService undeliveredParcelService;

    // 게시글 등록
    @PostMapping("/categories/{categoryId}/posts/undelivered-parcel")
    public ResponseEntity<ResTemplate<Void>> handleCreateParcelPost(
            @PathVariable Long categoryId, Principal principal
    ) {
        // 이미지 저장
        String email = PrincipalUtil.getEmail(principal);
        long unDeliParcelId = undelivParcelPostService.create(categoryId, email);
        ResTemplate<Void> resTemplate = new ResTemplate<>(HttpStatus.CREATED, unDeliParcelId + "번 게시글 작성");

        return ResponseEntity
                .created(URI.create("/categories/"+categoryId+"/posts/undelivered-parcel/" + unDeliParcelId))
                .body(resTemplate);
    }

    // 게시글 목록 조회
//    @GetMapping("/categories/{categoryId}/posts/undelivered-parcel")
//    public ResTemplate<?> handleGetParcelPosts(
//            @PathVariable Long categoryId
//    ) {
//        ResTemplate<?> resTemplate = new ResTemplate<>(HttpStatus.OK,
//                undelivParcelPostService.getAllByCategoryId(categoryId));
//        return ResponseEntity.ok(resTemplate);
//    }

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
    @PostMapping("/categories/{categoryId}/posts/undelivered-parcel/{undelivParcelPostId}/register")
    public ResponseEntity<ResTemplate<Void>> handleCreateRegister(
            @PathVariable Long categoryId, @PathVariable Long undelivParcelPostId
            , @RequestBody CreateUndelivParcelDto.Req reqDto
            ) {
        undeliveredParcelService.create(undelivParcelPostId, reqDto);

        ResTemplate<Void> resTemplate = new ResTemplate<>(HttpStatus.CREATED,
                "관리대장에 " + undelivParcelPostId + "번 관리물품 작성");
        return ResponseEntity.status(HttpStatus.CREATED).body(resTemplate);
    }

}
