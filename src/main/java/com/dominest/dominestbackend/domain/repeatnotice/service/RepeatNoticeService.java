package com.dominest.dominestbackend.domain.repeatnotice.service;

import com.dominest.dominestbackend.api.repeatnotice.request.RepeatNoticeSaveRequest;
import com.dominest.dominestbackend.api.repeatnotice.response.RepeatNoticeSaveResponse;
import com.dominest.dominestbackend.domain.repeatnotice.RepeatNotice;
import com.dominest.dominestbackend.domain.repeatnotice.repository.RepeatNoticeRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private String convertDayOfWeek(int dayInNumber) {
        switch(dayInNumber) {
            case 1:
                return "월요일";
            case 2:
                return "화요일";
            case 3:
                return "수요일";
            case 4:
                return "목요일";
            case 5:
                return "금요일";
            case 6:
                return "토요일";
            case 7:
                return "일요일";
            default:
                throw new BusinessException(ErrorCode.NOT_CORRECT_DAY);
        }
    }
    public List<String> getDayNoticeContent(int requestDayOfWeek, LocalTime requestTime) {
        List<String> contents = new ArrayList<>();

        // 유저가 생성한 알림 가져오기
        List<RepeatNotice> allDayNotices = repeatNoticeRepository.findAll();

        // 요일 정보 가져오기 (1 = 월요일, 7 = 일요일)
        String currentDay = convertDayOfWeek(requestDayOfWeek);

        for (RepeatNotice repeatNotice : allDayNotices) {

            String noticeDay = repeatNotice.getDay(); // 알림이 설정된 요일

            // 요청한 시간이 알림을 보내야 하는 요일이라면
            if (noticeDay.equals(currentDay)) {
                LocalTime noticeTime = repeatNotice.getTime(); // 알림의 시간 설정

                // 요청 시간과 알림 시간이 같거나 알림 시간에서 X분 전의 시간을 뺐을 때 요청 시간과 같다면
                if (noticeTime.minusMinutes(repeatNotice.getAlertBefore()).equals(requestTime)
                        || noticeTime.equals(requestTime)) {
                    contents.add(repeatNotice.getContent());
                }
            }
        }

        return contents;
    }


}