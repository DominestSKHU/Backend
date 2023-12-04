package com.dominest.dominestbackend.api.dateNotice.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.dateNotice.request.DateNoticeSaveRequest;
import com.dominest.dominestbackend.api.dateNotice.response.DateNoticeResponse;
import com.dominest.dominestbackend.domain.dateNotice.DateNotice;
import com.dominest.dominestbackend.domain.dateNotice.service.DateNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DateNoticeController {

    private final DateNoticeService dateNoticeService;

    @PostMapping("/date-notice")
    public RspTemplate<DateNotice> createDateNotification(@RequestBody DateNoticeSaveRequest request) {
        DateNotice dateNotice = dateNoticeService.createDateNotice(request);

        return new RspTemplate<>(HttpStatus.OK
                , "알림이 성공적으로 생성되었습니다.", dateNotice);
    }

    @GetMapping("/content")
    public RspTemplate<List<String>> getNoticeContent(Principal principal,
                                                         @RequestParam("time")
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime time) {
        List<String> content = dateNoticeService.getDateNoticeContent(principal.getName(), time);

        return new RspTemplate<>(HttpStatus.OK
                , "알림을 성공적으로 불러왔습니다.", content);
    }

    @GetMapping("/notices")
    public RspTemplate<List<DateNoticeResponse>> getNoticesByUser(Principal principal) {
        List<DateNoticeResponse> noticeResponses = dateNoticeService.getDateNoticesByUser(principal);
        
        return new RspTemplate<>(HttpStatus.OK
                , "유저가 작성한 모든 알림을 성공적으로 불러왔습니다.", noticeResponses);
    }

    @PutMapping("/change-apply/{id}")
    public RspTemplate<Boolean> switchApply(@PathVariable Long id) {
        boolean changeApply =  dateNoticeService.switchDateApply(id);

        return new RspTemplate<>(HttpStatus.OK
                , id + "번의 알림 상태를 성공적으로 변경하였습니다.", changeApply);
    }

}
