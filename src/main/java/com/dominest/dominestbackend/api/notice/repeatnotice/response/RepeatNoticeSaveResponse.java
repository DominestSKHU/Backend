package com.dominest.dominestbackend.api.notice.repeatnotice.response;

import com.dominest.dominestbackend.domain.notice.repeatnotice.RepeatNotice;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class RepeatNoticeSaveResponse {
    private Long id;

    private String day; // 요일

    private LocalTime time; // 알림 시간

    private int alertBefore; // 몇 분 전 알림인지

    private String content; // 알림 내용

    private boolean apply;  // 적용 여부

    private String createdBy;

    private LocalDateTime createTime;


    public static RepeatNoticeSaveResponse of(Long id, String day, LocalTime time, int alertBefore,
                                              String content, boolean apply, String createdBy, LocalDateTime createTime){
        return new RepeatNoticeSaveResponse(id, day, time, alertBefore, content, apply, createdBy, createTime);
    }

    public RepeatNoticeSaveResponse(Long id, String day, LocalTime time, int alertBefore, String content, boolean apply) {
        this.id = id;
        this.day = day;
        this.time = time;
        this.alertBefore = alertBefore;
        this.content = content;
        this.apply = apply;
    }

    public static RepeatNoticeSaveResponse of(RepeatNotice repeatNotice) {
        return new RepeatNoticeSaveResponse(
                repeatNotice.getId(),
                repeatNotice.getDay(),
                repeatNotice.getTime(),
                repeatNotice.getAlertBefore(),
                repeatNotice.getContent(),
                repeatNotice.isApply());
    }


}
