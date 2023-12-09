package com.dominest.dominestbackend.api.repeatschedule.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.repeatschedule.request.RepeatScheduleSaveRequest;
import com.dominest.dominestbackend.api.repeatschedule.resopnse.AllRepeatScheduleResponse;
import com.dominest.dominestbackend.api.repeatschedule.resopnse.RepeatScheduleResponse;
import com.dominest.dominestbackend.domain.repeatschedule.service.RepeatScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RepeatScheduleController {

    private final RepeatScheduleService repeatScheduleService;

    @PostMapping("/repeat-schedule") // 반복일정 글 저장
    public RspTemplate<RepeatScheduleResponse> createRepeatedSchedule(@RequestBody RepeatScheduleSaveRequest request) {
        RepeatScheduleResponse createdRepeatedSchedule = repeatScheduleService.createDayNotices(request);

        return new RspTemplate<>(HttpStatus.OK
                , "반복일정 글이 성공적으로 생성되었습니다.", createdRepeatedSchedule);
    }

    @GetMapping("/repeat-schedules")
    public RspTemplate<List<AllRepeatScheduleResponse>> getAllRepeatSchedules() { // 모든 반복일정 글
        List<AllRepeatScheduleResponse> allRepeatScheduleResponses =  repeatScheduleService.getAllRepeatSchedules();
        return new RspTemplate<>(HttpStatus.OK
                , "모든 반복일정 글을 성공적으로 불러왔습니다.", allRepeatScheduleResponses);
    }

    @GetMapping("/repeat-schedule/{repeatScheduleId}/detail")
    public RspTemplate<RepeatScheduleResponse> getRepeatScheduleById(@PathVariable Long repeatScheduleId) {
        RepeatScheduleResponse repeatScheduleByIdResponse =  repeatScheduleService.getRepeatScheduleById(repeatScheduleId);

        return new RspTemplate<>(HttpStatus.OK
                , "해당 반복일정 글을 성공적으로 불러왔습니다.", repeatScheduleByIdResponse);
    }
}
