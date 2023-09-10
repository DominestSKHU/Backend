package com.dominest.dominestbackend.api.post.cardkey.dto;

import com.dominest.dominestbackend.domain.post.cardkey.CardKey;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCardKeyDto {
    @NoArgsConstructor
    @Getter
    public static class Req {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @NotNull(message = "카드키 발급일자를 입력해주세요")
        LocalDate issuedDate; // 카드키 발급일자
        @NotBlank(message = "기숙사 방 번호를 입력해주세요.")
        String roomNo; // N호실
        @NotBlank(message = "이름을 입력해주세요.")
        String name; // 이름
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dateOfBirth; // 생년월일
        @NotNull(message = "재발급 횟수를 입력해주세요.")
        @PositiveOrZero(message = "재발급 횟수는 0 이상이어야 합니다.")
        Integer reIssueCnt; // 재발급 횟수

        String etc = ""; // 비고. 검증하지 않고 값이 없으면 EMPTY STRING 처리함.

        public CardKey toEntity(User user, Category category) {
            return CardKey.builder()
                    .issuedDate(issuedDate)
                    .roomNo(roomNo)
                    .name(name)
                    .dateOfBirth(dateOfBirth)
                    .reIssueCnt(reIssueCnt)
                    .etc(etc)
                    .writer(user)
                    .category(category)
                    .build();
        }
    }
}
