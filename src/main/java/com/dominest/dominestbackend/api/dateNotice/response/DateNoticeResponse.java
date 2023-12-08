package com.dominest.dominestbackend.api.dateNotice.response;

import com.dominest.dominestbackend.domain.dateNotice.DateNotice;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class DateNoticeResponse {
    private Long id;
    private LocalDate date; // 일자
    private LocalTime time; // 알림 시간
    private int alertBefore; // 몇 분 전 알림인지
    private String content; // 내용
    private Boolean apply; // 적용여부

    public DateNoticeResponse(DateNotice dateNotice) {
        this.id = dateNotice.getId();
        this.date = dateNotice.getDate();
        this.time = dateNotice.getTime();
        this.alertBefore = dateNotice.getAlertBefore();
        this.content = dateNotice.getContent();
        this.apply = dateNotice.isApply();
    }

    public static DateNoticeResponse of(DateNotice dateNotice) {
        DateNoticeResponse response = new DateNoticeResponse();
        response.id = dateNotice.getId();
        response.date = dateNotice.getDate();
        response.time = dateNotice.getTime();
        response.alertBefore = dateNotice.getAlertBefore();
        response.content = dateNotice.getContent();
        response.apply = dateNotice.isApply();
        return response;
    }

    public static DateNoticeResponse of(Long id, LocalDate date, LocalTime time,
                                            int alertBefore, String content, boolean apply){
        return new DateNoticeResponse(id, date, time, alertBefore, content, apply);
    }

}
