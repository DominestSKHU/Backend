package com.dominest.dominestbackend.api.schedule.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UserScheduleResponse {
    private String name;

    private String phoneNumber;

}