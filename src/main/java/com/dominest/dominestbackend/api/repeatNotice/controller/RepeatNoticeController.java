package com.dominest.dominestbackend.api.repeatNotice.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.repeatNotice.request.RepeatNoticeSaveRequest;
import com.dominest.dominestbackend.api.repeatNotice.response.RepeatNoticeSaveResponse;
import com.dominest.dominestbackend.domain.repeatNotice.service.RepeatNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RepeatNoticeController {

    private final RepeatNoticeService repeatNoticeService;

    @PostMapping("/repeat-notice")
    public RspTemplate<List<RepeatNoticeSaveResponse>> createDayNotices(@RequestBody List<RepeatNoticeSaveRequest> requests) {
        List<RepeatNoticeSaveResponse> repeatNotices = repeatNoticeService.createDayNotices(requests);

        return new RspTemplate<>(HttpStatus.OK
                , "알림이 성공적으로 생성되었습니다.", repeatNotices);
    }


    @GetMapping("/repeat-notice")
    public ResponseEntity<List<String>> getDayNoticeContent(@RequestParam String createBy, @RequestParam String requestTime) {
        LocalDateTime dateTime = LocalDateTime.parse(requestTime);
        List<String> contents = repeatNoticeService.getDayNoticeContent(createBy, dateTime);
        return ResponseEntity.ok(contents);
    }
}
