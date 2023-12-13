package com.dominest.dominestbackend.domain.repeatnotice;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.repeatschedule.RepeatSchedule;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RepeatNotice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repeat_schedule_id")
    private RepeatSchedule repeatSchedule;

    public void updateRepeatSchedule(RepeatSchedule repeatSchedule) {
        this.repeatSchedule = repeatSchedule;
    }

    @Builder
    public RepeatNotice (String day, LocalTime time,
                      int alertBefore, String content, boolean apply){ // 요일별 알림
        this.day = day;
        this.time = time;
        this.alertBefore = alertBefore;
        this.content = content;
        this.apply = true;
    }

}
