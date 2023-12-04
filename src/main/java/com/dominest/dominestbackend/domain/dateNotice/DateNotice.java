package com.dominest.dominestbackend.domain.dateNotice;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DateNotice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date; // 일자

    private String day; // 요일
    @Column(nullable = false)
    private LocalTime time; // 알림 시간

    @Column(nullable = false)
    private int alertBefore; // 몇 분 전 알림인지

    @Column(nullable = false)
    private String content; // 알림 내용

    @Column(nullable = false)
    private boolean apply;  // 적용 여부

    public boolean switchApply() {
        this.apply = !this.apply;
        return this.apply;
    }


    @Builder
    public DateNotice(LocalDate date, LocalTime time,
                      int alertBefore, String content, boolean apply){ // 일자, 시간별 알림
        this.date = date;
        this.time = time;
        this.alertBefore = alertBefore;
        this.content = content;
        this.apply = apply;

    }

}
