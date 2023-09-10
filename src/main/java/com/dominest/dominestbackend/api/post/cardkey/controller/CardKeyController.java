package com.dominest.dominestbackend.api.post.cardkey.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.post.cardkey.dto.CardKeyListDto;
import com.dominest.dominestbackend.api.post.cardkey.dto.CreateCardKeyDto;
import com.dominest.dominestbackend.api.post.cardkey.dto.UpdateCardKeyDto;
import com.dominest.dominestbackend.domain.post.cardkey.CardKey;
import com.dominest.dominestbackend.domain.post.cardkey.CardKeyService;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.global.util.PageableUtil;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class CardKeyController {
    private final CardKeyService cardKeyService;
    private final CategoryService categoryService;

    // 등록
    @PostMapping("/categories/{categoryId}/posts/card-key")
    public ResponseEntity<RspTemplate<Void>> handleCreateComplaint(
            @RequestBody @Valid CreateCardKeyDto.Req reqDto
            , @PathVariable Long categoryId, Principal principal
    ) {
        String email = PrincipalUtil.toEmail(principal);
        long cardKeyId = cardKeyService.create(reqDto, categoryId, email);
        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED
                , cardKeyId + "번 카드키 기록 작성");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rspTemplate);
    }
    // 목록조회

    // 수정
    @PatchMapping("/card-keys/{cardKeyId}")
    public RspTemplate<Void> handleUpdateCardKey(
            @PathVariable Long cardKeyId, @RequestBody @Valid UpdateCardKeyDto.Req reqDto
    ) {
        // parcelId 조회, 값 바꿔치기, 저장하기
        long updatedKeyId = cardKeyService.update(cardKeyId, reqDto);

        return new RspTemplate<>(HttpStatus.OK, updatedKeyId + "번 카드키 기록 수정");
    }

    // 삭제
    // 민원 삭제
    @DeleteMapping("/card-keys/{cardKeyId}")
    public RspTemplate<Void> handleDeleteCardKey(
            @PathVariable Long cardKeyId
    ) {
        long deletedKeyId = cardKeyService.delete(cardKeyId);

        return new RspTemplate<>(HttpStatus.OK, deletedKeyId + "번 카드키 기록 삭제");
    }
    // 이름검색 (인덱스 만들고, '검색어%' 로 만들 것
    // 민원 목록 조회. 최신등록순
    @GetMapping("/categories/{categoryId}/posts/card-key")
    public RspTemplate<CardKeyListDto.Res> handleGetCardKeys(
            @PathVariable Long categoryId, @RequestParam(defaultValue = "1") int page
            , @RequestParam(required = false) String name
    ) {
        final int COMPLAINT_TYPE_PAGE_SIZE = 20;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageableUtil.of(page, COMPLAINT_TYPE_PAGE_SIZE, sort);

        Category category = categoryService.validateCategoryType(categoryId, Type.CARD_KEY);

        Page<CardKey> cardKeyPage = cardKeyService.getPage(category.getId(), pageable, name);

        CardKeyListDto.Res resDto = CardKeyListDto.Res.from(cardKeyPage, category);
        return new RspTemplate<>(HttpStatus.OK
                , "(생성일자 내림차순) 페이지  목록 조회 - " + resDto.getPage().getCurrentPage() + "페이지"
                ,resDto);
    }

    // 이름검색까지 끝내고 민원대장에 이름
    // 배포하고나서 테스트 오지게 돌리기 싫으면 테스트코드 성공케이스만이라도 일단 작성하자.
}
