package com.dominest.dominestbackend.api.notice.repeatschedule.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RepeatScheduleSaveRequest {
    private String title;

    private String description;

    private List<Long> repeatNoticeIds; // RepeatNotice의 ID 리스트
}

