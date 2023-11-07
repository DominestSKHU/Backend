package com.dominest.dominestbackend.api.schedule.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.schedule.request.ScheduleDeleteRequest;
import com.dominest.dominestbackend.api.schedule.request.ScheduleSaveRequest;
import com.dominest.dominestbackend.api.schedule.response.ScheduleInfo;
import com.dominest.dominestbackend.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping ("/schedule")// 스케줄 저장
    public RspTemplate<Void> addOrUpdateSchedules(@RequestBody ScheduleSaveRequest requests) {
        scheduleService.saveSchedule(requests);
        return new RspTemplate<>(HttpStatus.OK
                , requests.getDayOfWeek()+ "의 " + requests.getStartTime() + " ~ " + requests.getEndTime() + "시간대에 "
                +requests.getUsername() + "님의 스케줄을 성공적으로 저장하였습니다.");
    }

    @GetMapping("/schedules") // 저장된 모든 스케줄 불러오기
    public RspTemplate<List<ScheduleInfo>> getSchedule() {
        List<ScheduleInfo> scheduleInfos = scheduleService.getSchedule();
        return new RspTemplate<>(HttpStatus.OK
                , "모든 스케줄을 성공적으로 불러왔습니다.", scheduleInfos);
    }

    @DeleteMapping("/schedule") // 스케줄 삭제
    public RspTemplate<Void> deleteSchedule(@RequestBody ScheduleDeleteRequest request) {
        scheduleService.deleteSchedule(request);
        return new RspTemplate<>(HttpStatus.OK, request.getDayOfWeek()+ "의 " + request.getTimeSlot() + "시간대에 "
                +request.getUsername() + "님의 스케줄을 성공적으로 삭제했습니다.");
    }
}