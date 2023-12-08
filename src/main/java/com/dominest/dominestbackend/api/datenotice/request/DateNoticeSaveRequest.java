package com.dominest.dominestbackend.api.datenotice.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DateNoticeSaveRequest {
    private LocalDate date; // 일자

    private String time; // 알림 시간

    private int alertBefore; // 몇 분 전 알림인지

    private String content; // 알림 내용
}