package com.dominest.dominestbackend.api.post.undeliveredparcel.controller;

import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.UndeliveredParcelPostService;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.component.UndeliveredParcelService;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    // 게시글 내부 관리목록에 관리물품 등록
//    @PostMapping("/categories/{categoryId}/posts/undelivered-parcel/{undelivParcelPostId}/register")
//    public ResponseEntity<ResTemplate<Void>> handleCreateRegister(
//            @PathVariable Long categoryId, @PathVariable Long undelivParcelPostId
//            , @RequestBody
//    ) {
//        undeliveredParcelService.create(undelivParcelPostId);
//        ResTemplate<Void> resTemplate = new ResTemplate<>(HttpStatus.CREATED, undelivParcelPostId + "번 게시글 작성");
//
//        return ResponseEntity
//                .created(URI.create("/categories/"+categoryId+"/posts/undelivered-parcel/" + undelivParcelPostId))
//                .body(resTemplate);
//    }

}
