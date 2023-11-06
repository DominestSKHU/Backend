package com.dominest.dominestbackend.api.schedule.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScheduleDeleteRequest {
    private String username;
    private String dayOfWeek;
    private String timeSlot;
}
