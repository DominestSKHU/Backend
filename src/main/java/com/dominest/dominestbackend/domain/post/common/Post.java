package com.dominest.dominestbackend.domain.post.common;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 게시글의 공통 속성인 제목, 작성자, ID를 관리하는 클래스
 * post 패키지의 entity class 들은 이 클래스를 확장하도록 설계됨.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String writer;

    public Post(String title, String writer) {
        this.title = title;
        this.writer = writer;
    }
}
