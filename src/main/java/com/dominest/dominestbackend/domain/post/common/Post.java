package com.dominest.dominestbackend.domain.post.common;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.user.User;
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
    /*작성자의 이름이다. String으로 작성자이름만 넣을까 했으나,
      사용자를 특정할 수 있는 ID를 넣어야 하므로 User 객체를 넣어 외래키 연관관계를 맺도록 함.*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;

    protected Post(String title, User writer, Category category) {
        this.title = title;
        this.writer = writer;
        this.category = category;
    }
}
