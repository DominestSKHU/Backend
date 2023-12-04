package com.dominest.dominestbackend.domain.repeatNotice.service;

import com.dominest.dominestbackend.api.repeatNotice.request.RepeatNoticeSaveRequest;
import com.dominest.dominestbackend.api.repeatNotice.response.RepeatNoticeSaveResponse;
import com.dominest.dominestbackend.domain.repeatNotice.RepeatNotice;
import com.dominest.dominestbackend.domain.repeatNotice.repository.RepeatNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RepeatNoticeService {
    private final RepeatNoticeRepository repeatNoticeRepository;

    @Transactional
    public List<RepeatNoticeSaveResponse> createDayNotices(List<RepeatNoticeSaveRequest> requests) { // 알림 저장
        List<RepeatNotice> dayNotices = new ArrayList<>();

        for (RepeatNoticeSaveRequest request : requests) {
            LocalTime time = LocalTime.parse(request.getTime());

            RepeatNotice dayNotice = RepeatNotice.builder()
                    .day(request.getDay())
                    .time(time)
                    .alertBefore(request.getAlertBefore())
                    .content(request.getContent())
                    .apply(true)
                    .build();

            dayNotices.add(repeatNoticeRepository.save(dayNotice));
        }

        List<RepeatNoticeSaveResponse> responses = new ArrayList<>();

        for (RepeatNotice dayNotice : dayNotices) {
            RepeatNoticeSaveResponse response = RepeatNoticeSaveResponse.of(
                    dayNotice.getId(),
                    dayNotice.getDay(),
                    dayNotice.getTime(),
                    dayNotice.getAlertBefore(),
                    dayNotice.getContent(),
                    dayNotice.isApply(),
                    dayNotice.getCreatedBy(),
                    dayNotice.getCreateTime()
            );
            responses.add(response);
        }

        return responses;
    }


    // 알림 보내기
    public List<String> getDayNoticeContent(String createBy, LocalDateTime requestTime) {
        List<String> contents = new ArrayList<>();

        // 유저가 생성한 알림 가져오기
        List<RepeatNotice> allDayNotices = repeatNoticeRepository.findByCreatedBy(createBy);

        // 요일 정보 가져오기 (1 = Monday, 7 = Sunday)
        int currentDayOfWeek = requestTime.getDayOfWeek().getValue();

        for (RepeatNotice repeatNotice : allDayNotices) {
            // 알림이 설정된 요일 정보 가져오기
            int noticeDayOfWeek = DayOfWeek.valueOf(repeatNotice.getDay().toUpperCase()).getValue();

            // 요청한 시간이 알림을 보내야 하는 요일이라면
            if (noticeDayOfWeek == currentDayOfWeek) {
                LocalTime noticeTime = repeatNotice.getTime(); // 알림의 시간 설정

                // 요청 시간과 알림 시간이 같거나 알림 시간에서 X분 전의 시간을 뺐을 때 요청 시간과 같다면
                if (requestTime.toLocalTime().minusMinutes(repeatNotice.getAlertBefore()).equals(noticeTime)
                        || requestTime.toLocalTime().equals(noticeTime)) {
                    contents.add(repeatNotice.getContent());
                }
            }
        }

        return contents;
    }
}