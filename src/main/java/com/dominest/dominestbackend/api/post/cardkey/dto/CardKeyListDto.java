package com.dominest.dominestbackend.api.post.cardkey.dto;

import com.dominest.dominestbackend.api.common.AuditLog;
import com.dominest.dominestbackend.api.common.CategoryDto;
import com.dominest.dominestbackend.api.common.PageInfoDto;
import com.dominest.dominestbackend.domain.post.cardkey.CardKey;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardKeyListDto {

    @Getter
    public static class Res {
        CategoryDto category;
        PageInfoDto page;

        List<CardKeyDto> cardKeys;

        public static Res from(Page<CardKey> cardKeyPage, Category category) {
            CategoryDto categoryDto = CategoryDto.from(category);
            PageInfoDto pageInfoDto = PageInfoDto.from(cardKeyPage);

            List<CardKeyDto> complaints = CardKeyDto.from(cardKeyPage);
            return new Res(categoryDto, pageInfoDto, complaints);
        }

        public Res(CategoryDto category, PageInfoDto page, List<CardKeyDto> cardKeys) {
            this.category = category;
            this.page = page;
            this.cardKeys = cardKeys;
        }

    }

    @Builder
    @Getter
    static class CardKeyDto {
        long id;
        LocalDate issuedDate; // 카드키 발급일자
        String roomNo; // N호실
        String name; // 이름
        LocalDate dateOfBirth; // 생년월일
        Integer reIssueCnt; // 재발급 횟수

        String etc;     // 비고
        AuditLog auditLog; // 여기에 '작성자' 있음.

        static CardKeyDto from(CardKey cardKey){
            return CardKeyDto.builder()
                    .id(cardKey.getId())
                    .issuedDate(cardKey.getIssuedDate())
                    .roomNo(cardKey.getRoomNo())
                    .name(cardKey.getName())
                    .dateOfBirth(cardKey.getDateOfBirth())
                    .reIssueCnt(cardKey.getReIssueCnt())
                    .etc(cardKey.getEtc())
                    .auditLog(AuditLog.from(cardKey))
                    .build();
        }

        static List<CardKeyDto> from(Page<CardKey> cardKeyPage){
            return cardKeyPage
                    .map(CardKeyDto::from)
                    .toList();
        }
    }
}
