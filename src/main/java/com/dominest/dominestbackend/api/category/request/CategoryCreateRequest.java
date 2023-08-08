package com.dominest.dominestbackend.api.category.request;

import com.dominest.dominestbackend.domain.categorytype.Type;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CategoryCreateRequest {
    private String categoryName;
    private Type categoryType;
    private String explanation;
    private String email;
}