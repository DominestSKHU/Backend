package com.dominest.dominestbackend.api.dar.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class DarMonthResponse {
    private int day; // 1일 ~ 31일에 해당하는 id
    private boolean content; // 내용 유무
}