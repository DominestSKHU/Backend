package com.dominest.dominestbackend.api.repeatSchedule.resopnse;

import com.dominest.dominestbackend.api.repeatNotice.response.RepeatNoticeSaveResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class RepeatScheduleResponse {
    private long id;

    private String title;

    private String description;

    private String createdBy;

    private LocalDateTime createTime;

    private List<RepeatNoticeSaveResponse> repeatNoticeResponses;

    public static RepeatScheduleResponse of(Long id, String title, String description,
                                            String createdBy, LocalDateTime createTime,
                                            List<RepeatNoticeSaveResponse> repeatNoticeResponses){
        return new RepeatScheduleResponse(id, title, description, createdBy, createTime, repeatNoticeResponses);
    }
}