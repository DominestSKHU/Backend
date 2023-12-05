package com.dominest.dominestbackend.api.schedule.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/*
특정 시간대에 대한 정보 나타냄
각 시간대에는 특정 사용자가 할당될 수 있으며, 해당 정보를 함께 저장함
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class TimeSlotInfo {
    private String timeSlot;
    private List<String> usernames;
}
