package com.dominest.dominestbackend.api.category.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@NoArgsConstructor
@Getter
public class CategoryUpdateRequest {
    @NotNull(message = "카테고리 목록은 필수 입력 값입니다")
    @Valid
    List<CategoryDto> categories;

    @NoArgsConstructor
    @Getter
    public static class CategoryDto {
        @NotNull(message = "카테고리 ID는 필수 입력 값입니다")
        @Positive(message = "카테고리 ID는 0보다 커야합니다")
        Long id;
        @NotBlank(message = "카테고리 이름은 필수 입력 값입니다")
        String categoryName;
        String explanation = "";
        @NotNull(message = "카테고리 정렬 순서는 필수 입력 값입니다")
        @Positive(message = "카테고리 정렬 순서는 0보다 커야합니다")
        Integer orderKey;
    }
}
