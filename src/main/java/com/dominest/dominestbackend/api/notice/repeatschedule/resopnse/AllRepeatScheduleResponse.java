package com.dominest.dominestbackend.api.notice.repeatschedule.resopnse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AllRepeatScheduleResponse {
    private Long id;

    private String title;

    private String createdBy;

    private LocalDateTime createTime;

    public static AllRepeatScheduleResponse of(Long id, String title, String createdBy, LocalDateTime createTime){
        return new AllRepeatScheduleResponse(id, title, createdBy, createTime);
    }

}
