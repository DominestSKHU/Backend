package com.dominest.dominestbackend.api.schedule.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScheduleSaveRequest {
    private String dayOfWeek; // 요일

    private String timeSlot;  // 시간

    private List<String> usernames;
}
