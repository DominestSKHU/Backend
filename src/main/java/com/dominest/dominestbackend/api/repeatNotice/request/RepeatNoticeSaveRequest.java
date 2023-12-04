package com.dominest.dominestbackend.api.repeatNotice.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RepeatNoticeSaveRequest {
    private String day; // 요일

    private String time; // 알림 시간

    private int alertBefore; // 몇 분 전 알림인지

    private String content; // 알림 내용
}
