package com.dominest.dominestbackend.api.calendar.controller;

import com.dominest.dominestbackend.api.calendar.request.CalenderSaveRequest;
import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.domain.calender.Calender;
import com.dominest.dominestbackend.domain.calender.service.CalenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calender")
public class CalenderController {

    private final CalenderService calenderService;

    @PostMapping("/save") // 캘린더 저장
    public RspTemplate<String> saveCalender(@RequestBody @Valid CalenderSaveRequest request) {
        return calenderService.addCalender(request);
    }

    @GetMapping("/get/{date}") // 날짜 들어오면 일정 내보내기 없을 때 처리
    public List<Calender> getEventsByDate(@PathVariable("date") String dateString) {
        return calenderService.getByDate(dateString);
    }

    @DeleteMapping("/delete/{date}")
    public RspTemplate<String> deleteCalender(@PathVariable("date") String dateString) {
        return calenderService.deleteEventsByDate(dateString);
    }
}