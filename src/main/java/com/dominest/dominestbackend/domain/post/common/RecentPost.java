package com.dominest.dominestbackend.domain.post.common;


import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** 최근 게시글 작성 기록*/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RecentPost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String link;
    private String categoryLink;
    private Type categoryType;

    @Builder
    private RecentPost(String title, String link, String categoryLink, Type categoryType) {
        this.title = title;
        this.link = link;
        this.categoryLink = categoryLink;
        this.categoryType = categoryType;
    }
}

















