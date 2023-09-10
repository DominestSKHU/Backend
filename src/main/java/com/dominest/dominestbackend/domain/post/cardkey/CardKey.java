package com.dominest.dominestbackend.domain.post.cardkey;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 카드키 관리대장 페이지에서 쓰일 카드키 관리 엔티티
 * 객체 하나가 페이지상의 한 행에 대응됨.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CardKey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate issuedDate; // 카드키 발급날짜
    @Column(nullable = false)
    private String roomNo; // 호실

    @Column(nullable = false)
    private String name; // 이름
    @Column(nullable = false)
    private LocalDate dateOfBirth; // 생년월일

    @Column(nullable = false)
    private Integer reIssueCnt; // 재발급 횟수

    @Column(nullable = false)
    private String etc = ""; // 비고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "writer_id")
    private User writer;    // 작성자 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;

    @Builder
    private CardKey(LocalDate issuedDate, String roomNo, String name, LocalDate dateOfBirth, Integer reIssueCnt, String etc, User writer, Category category) {
        this.issuedDate = issuedDate;
        this.roomNo = roomNo;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.reIssueCnt = reIssueCnt;
        this.etc = etc;
        this.writer = writer;
        this.category = category;
    }

    public void updateValues(LocalDate issuedDate, String roomNo, String name, LocalDate dateOfBirth, Integer reIssueCnt, String etc) {
        this.issuedDate = issuedDate;
        this.roomNo = roomNo;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.reIssueCnt = reIssueCnt;
        this.etc = etc;
    }
}
