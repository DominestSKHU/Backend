package com.dominest.dominestbackend.domain.post.component.category;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.component.category.component.categorytype.Type;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Category")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String categoryName; // 카테고리 이름

    @Enumerated(EnumType.STRING)
    private Type categoryType;

    @Column(length = 255, nullable = false)
    private String explanation; // 카테고리 상세설명

    @Builder
    public Category(String categoryName, Type categoryType, String explanation) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.explanation = explanation;
    }

    public void updateCategory(String updatedCategoryName) {
        this.categoryName = updatedCategoryName;
    }
}