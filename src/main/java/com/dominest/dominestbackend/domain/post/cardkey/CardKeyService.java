package com.dominest.dominestbackend.domain.post.cardkey;

import com.dominest.dominestbackend.api.post.cardkey.dto.CreateCardKeyDto;
import com.dominest.dominestbackend.api.post.cardkey.dto.UpdateCardKeyDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import com.dominest.dominestbackend.domain.post.component.category.service.CategoryService;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.service.UserService;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CardKeyService {
    private final CardKeyRepository cardKeyRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional
    public long create(CreateCardKeyDto.Req reqDto, Long categoryId, String email) {
        // CardKey 연관 객체인 category, user 찾기
        User user = userService.getUserByEmail(email);
        // CardKey 연관 객체인 category 찾기
        Category category = categoryService.validateCategoryType(categoryId, Type.CARD_KEY);

        CardKey cardKey = reqDto.toEntity(user, category);

        // CardKey 객체 생성 후 저장
        return cardKeyRepository.save(cardKey).getId();
    }

    @Transactional
    public long update(Long cardKeyId, UpdateCardKeyDto.Req reqDto) {
        CardKey cardKey = getById(cardKeyId);

        cardKey.updateValues(
                reqDto.getIssuedDate()
                , reqDto.getRoomNo()
                , reqDto.getName()
                , reqDto.getDateOfBirth()
                , reqDto.getReIssueCnt()
                , reqDto.getEtc()
        );
        return cardKey.getId();
    }

    public CardKey getById(Long id) {
        return EntityUtil.mustNotNull(cardKeyRepository.findById(id), ErrorCode.CARD_KEY_NOT_FOUND);
    }

    @Transactional
    public long delete(Long id) {
        CardKey cardKey = getById(id);
        cardKeyRepository.delete(cardKey);

        return cardKey.getId();
    }

    public Page<CardKey> getPage(Long id, Pageable pageable, String name) {
        if (StringUtils.hasText(name)) {
            return cardKeyRepository.findAllByCategoryIdAndNameStartsWith(id, name, pageable);
        }
        return cardKeyRepository.findAllByCategoryId(id, pageable);
    }
}
