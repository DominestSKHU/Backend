package com.dominest.dominestbackend.domain.post.cardkey;

import com.dominest.dominestbackend.api.post.cardkey.dto.CreateCardKeyDto;
import com.dominest.dominestbackend.api.post.cardkey.dto.UpdateCardKeyDto;
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

    @Transactional
    public long create(CreateCardKeyDto.Req reqDto, Long categoryId, String email) {
        return 0;
    }

    @Transactional
    public long update(Long cardKeyId, UpdateCardKeyDto.Req reqDto) {
        return 0;
    }

    @Transactional
    public long delete(Long cardKeyId) {
        return 0;
    }

    public Page<CardKey> getPage(Long id, Pageable pageable, String name) {
        if (StringUtils.hasText(name)) {
            return cardKeyRepository.findAllByCategoryIdAndNameStartsWith(id, name, pageable);
        }
        return cardKeyRepository.findAllByCategoryId(id, pageable);
    }
}
