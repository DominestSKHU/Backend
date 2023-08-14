package com.dominest.dominestbackend.domain.post.common;

import com.dominest.dominestbackend.domain.common.BaseEntity;
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
      토큰 인증방식이라 '상태'를 서버에서 관리할 수 없어, 삭제된 사용자의 정보가 들어갈 가능성이 있으므로
      User 객체를 넣어 외래키 연관관계를 맺도록 함.*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "writer_id")
    private User writer;

    public Post(String title, User writer) {
        this.title = title;
        this.writer = writer;
    }
}
