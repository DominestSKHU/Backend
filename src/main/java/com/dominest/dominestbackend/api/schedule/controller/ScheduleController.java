package com.dominest.dominestbackend.api.schedule.controller;

import com.dominest.dominestbackend.api.schedule.request.ScheduleSaveRequest;
import com.dominest.dominestbackend.domain.schedule.repository.ScheduleRepository;
import com.dominest.dominestbackend.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleRepository scheduleRepository;

    private final ScheduleService scheduleService;

    @PostMapping("/save") // 스케줄 저장
    public ResponseEntity<String> addOrUpdateSchedules(@RequestBody ScheduleSaveRequest requests) {
        scheduleService.saveSchedule(requests);
        return ResponseEntity.ok(requests.getDayOfWeek()+ "의 " + requests.getTimeSlot() + "시간대에 "
                +requests.getUsernames() + "님의 스케줄을 성공적으로 저장하였습니다.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getSchedule() {
        List<Map<String, Object>> scheduleInfo = scheduleService.getSchedule();
        return ResponseEntity.ok(scheduleInfo);
    }
}