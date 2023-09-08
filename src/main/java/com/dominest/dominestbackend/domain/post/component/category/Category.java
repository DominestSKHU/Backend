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
@Table(indexes = {
        @Index(name = "idx_category_orderKey"
                    , columnList = "order_key, type, name, id, explanation") // covering index
        })
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name; // 카테고리 이름

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private String explanation; // 카테고리 상세설명

    @Column(nullable = false)
    private Integer orderKey;   // 정렬 기준

    @Builder
    public Category(String name, Type type, String explanation, Integer orderKey) {
        this.name = name;
        this.type = type;
        this.explanation = explanation;
        this.orderKey = orderKey;
    }

    public String getPostsLink(){
        return  "/categories/" + getId() + "/posts/" + getType().getUrl();
    }

    public void updateCategory(String updatedCategoryName) {
        this.name = updatedCategoryName;
    }
}