package com.dominest.dominestbackend.api.schedule.request;

import com.dominest.dominestbackend.domain.schedule.Schedule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScheduleDeleteRequest {
    private String username;
    private Schedule.DayOfWeek dayOfWeek;
    private String timeSlot;
}
