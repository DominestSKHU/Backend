package com.dominest.dominestbackend.api.schedule.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.schedule.request.ScheduleSaveRequest;
import com.dominest.dominestbackend.api.schedule.response.UserScheduleResponse;
import com.dominest.dominestbackend.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/save") // 스케줄 저장
    public RspTemplate<Void> addOrUpdateSchedules(@RequestBody ScheduleSaveRequest requests) {
        scheduleService.saveSchedule(requests);
        return new RspTemplate<>(HttpStatus.OK
                , requests.getDayOfWeek()+ "의 " + requests.getTimeSlot() + "시간대에 "
                +requests.getUsernames() + "님의 스케줄을 성공적으로 저장하였습니다.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getSchedule() {
        List<Map<String, Object>> scheduleInfo = scheduleService.getSchedule();
        return ResponseEntity.ok(scheduleInfo);
    }

    @GetMapping("/user-info")
    public RspTemplate<List<UserScheduleResponse>> getUserInfo() {
        List<UserScheduleResponse> userResponses = scheduleService.getUserInfo();
        return new RspTemplate<>(HttpStatus.OK, "유저의 이름과 번호를 성공적으로 불러왔습니다.", userResponses);
    }

}