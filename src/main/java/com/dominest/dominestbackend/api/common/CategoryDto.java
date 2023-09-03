package com.dominest.dominestbackend.api.common;

import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDto {
    long id;
    String categoryName;
    Type type;

    public static CategoryDto from(Category category){
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getName())
                .type(category.getType())
                .build();
    }
}
