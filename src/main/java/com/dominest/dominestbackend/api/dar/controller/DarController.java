package com.dominest.dominestbackend.api.dar.controller;

import com.dominest.dominestbackend.api.dar.request.DarSaveRequest;
import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.dar.response.DarMonthResponse;
import com.dominest.dominestbackend.domain.dar.Dar;
import com.dominest.dominestbackend.domain.dar.service.DarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dar")
public class DarController {

    private final DarService darService;

    @PostMapping("/content") // 일정 저장 postMapping이므로 url에 save 삭제
    public RspTemplate<String> saveDar(@RequestBody @Valid DarSaveRequest request) {
        darService.addDar(request);
        return new RspTemplate<>(HttpStatus.OK
                , "일정을 저장했습니다.", request.getDate() + " 날짜의 [" + request.getContent() + "] 내용을 저장하였습니다.");
    }


    @GetMapping("/{date}") // 날짜 들어오면 일정 내보내기
    public RspTemplate<List<Dar>> getEventsByDate(@PathVariable("date") String dateString) {
        List<Dar> events = darService.getByDate(dateString);
        return new RspTemplate<>(HttpStatus.OK, dateString +"의 일정을 성공적으로 가져왔습니다.", events);

    }

    @GetMapping("/list/{date}") // 월별 일정 내보내기
    public RspTemplate<List<DarMonthResponse>> getEventsByMonth(@PathVariable("date") String dateString) {
        List<DarMonthResponse> darList = darService.getByMonth(dateString);
        return new RspTemplate<>(HttpStatus.OK, dateString +"월의 일정을 성공적으로 가져왔습니다.", darList);
    }

    @DeleteMapping("/{date}") // 일정 삭제. DeleteMapping 이므로 url에 delete 삭제
    public RspTemplate<Void> deleteCalender(@PathVariable("date") String dateString) {
        darService.deleteEventsByDate(dateString);
        return new RspTemplate<>(HttpStatus.OK, dateString + " 날짜의 일정을 삭제하였습니다.");

    }
}