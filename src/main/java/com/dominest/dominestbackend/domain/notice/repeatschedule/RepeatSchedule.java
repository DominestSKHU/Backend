package com.dominest.dominestbackend.domain.notice.repeatschedule;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.notice.repeatnotice.RepeatNotice;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
반복 알림의 글
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RepeatSchedule extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String title; // 제목

    private String description; // 내용

    @OneToMany(mappedBy = "repeatSchedule", cascade = CascadeType.PERSIST)
    private List<RepeatNotice> repeatNotices = new ArrayList<>(); // 반복일정 내용

    public void addRepeatNotice(RepeatNotice repeatNotice) {
        if (this.repeatNotices == null) {
            this.repeatNotices = new ArrayList<>();
        }
        this.repeatNotices.add(repeatNotice);
        repeatNotice.updateRepeatSchedule(this);
    }


    @Builder
    public RepeatSchedule(String title, String description, List<RepeatNotice> repeatNotices){
        this.title = title;
        this.description = description;
        this.repeatNotices = repeatNotices;
    }
}
