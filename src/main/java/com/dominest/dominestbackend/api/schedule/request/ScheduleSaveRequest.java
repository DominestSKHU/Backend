package com.dominest.dominestbackend.api.schedule.request;

import com.dominest.dominestbackend.domain.schedule.Schedule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScheduleSaveRequest {

    private String username; // 유저 이름

    private Schedule.DayOfWeek dayOfWeek; // 요일

    private String startTime;  // 시작 시간

    private String endTime;  // 끝나는 시간
}
