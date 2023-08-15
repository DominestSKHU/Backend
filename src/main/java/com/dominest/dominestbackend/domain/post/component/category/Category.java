package com.dominest.dominestbackend.domain.post.component.category;

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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String categoryName; // 카테고리 이름

    @Enumerated(EnumType.STRING)
    private Type categoryType;

    @Column(length = 10000, nullable = false)
    private String explanation; // 카테고리 상세설명

    private String name; // 유저 이름 저장

//    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Post> posts = new ArrayList<>(); // 카테고리 내 글들

    @Builder
    public Category(String categoryName, Type categoryType, String explanation, String name) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.explanation = explanation;
        this.name = name;
    }

    public void updateCategory(String updatedCategoryName) {
        this.categoryName = updatedCategoryName;
    }

    public void updateEditUser(String name){
        this.name = name;
    }
}