package com.dominest.dominestbackend.api.calendar.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CalendarSaveRequest {
    private LocalDate date; // 날짜

    private String content; // 내용 작성

}