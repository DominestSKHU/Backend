package com.dominest.dominestbackend.api.category.request;

import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class CreateCategoryRequest {
    @NotBlank(message = "카테고리 이름은 필수 입력 값입니다")
    @Length(max = 255, message = "카테고리 이름은 255자 이내로 입력해주세요.")
    private String categoryName;
    @NotNull(message = "카테고리 타입은 필수 입력 값입니다")
    private Type categoryType;
    private String explanation = "";
}