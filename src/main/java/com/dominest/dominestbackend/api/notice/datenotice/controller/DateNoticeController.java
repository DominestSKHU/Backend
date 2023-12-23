package com.dominest.dominestbackend.api.notice.datenotice.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.notice.datenotice.request.DateNoticeSaveRequest;
import com.dominest.dominestbackend.api.notice.datenotice.response.DateNoticeResponse;
import com.dominest.dominestbackend.domain.notice.datenotice.DateNotice;
import com.dominest.dominestbackend.domain.notice.datenotice.service.DateNoticeService;
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

    @GetMapping("/date-notices")
    public RspTemplate<List<String>> getNoticeContent(Principal principal,
                                                         @RequestParam("time")
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime time) {
        List<String> content = dateNoticeService.getDateNoticeContent(principal.getName(), time);

        return new RspTemplate<>(HttpStatus.OK
                , "알림을 성공적으로 불러왔습니다.", content);
    }

    @GetMapping("/date-notices/me")
    public RspTemplate<List<DateNoticeResponse>> getNoticesByUser(Principal principal) {
        List<DateNoticeResponse> noticeResponses = dateNoticeService.getDateNoticesByUser(principal);
        
        return new RspTemplate<>(HttpStatus.OK
                , "유저가 작성한 모든 알림을 성공적으로 불러왔습니다.", noticeResponses);
    }

    @PutMapping("/date-notices/{id}/change-apply")
    public RspTemplate<Boolean> switchApply(@PathVariable Long id) {
        boolean changeApply =  dateNoticeService.switchDateApply(id);

        return new RspTemplate<>(HttpStatus.OK
                , id + "번의 알림 상태를 성공적으로 변경하였습니다.", changeApply);
    }

}
