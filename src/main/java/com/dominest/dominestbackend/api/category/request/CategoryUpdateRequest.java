package com.dominest.dominestbackend.api.category.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CategoryUpdateRequest {
    private Long id;
    private String categoryName;
}
