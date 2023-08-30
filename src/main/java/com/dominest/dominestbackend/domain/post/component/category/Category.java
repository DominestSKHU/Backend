package com.dominest.dominestbackend.domain.post.component.category;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
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
    private String name; // 카테고리 이름

    private int position; // 순서

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(length = 255, nullable = false)
    private String explanation; // 카테고리 상세설명

    @Builder
    public Category(String name, Type type, String explanation, int position) {
        this.name = name;
        this.type = type;
        this.explanation = explanation;
        this.position = position;
    }

    public String getPostsLink(){
        return  "/categories/" + getId() + "/posts/" + getType().getUrl();
    }

    public void updateCategory(String updatedCategoryName) {
        this.name = updatedCategoryName;
    }

    public void updatePosition(int position) {
        this.position = position;
    }
}