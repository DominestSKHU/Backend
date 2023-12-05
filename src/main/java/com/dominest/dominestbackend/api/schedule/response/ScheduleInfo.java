package com.dominest.dominestbackend.api.schedule.response;

import com.dominest.dominestbackend.domain.schedule.Schedule;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/*
특정 요일의 스케줄 정보 나타냄
각 요일에는 여러 개의 시간대가 존재할 수 있고, 해당 시간대를 함께 저장함
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ScheduleInfo {
    private Schedule.DayOfWeek dayOfWeek;
    private List<TimeSlotInfo> timeSlotInfos;
}