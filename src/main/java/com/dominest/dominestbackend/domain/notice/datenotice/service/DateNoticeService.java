package com.dominest.dominestbackend.domain.notice.datenotice.service;

import com.dominest.dominestbackend.api.notice.datenotice.request.DateNoticeSaveRequest;
import com.dominest.dominestbackend.api.notice.datenotice.response.DateNoticeResponse;
import com.dominest.dominestbackend.domain.notice.datenotice.DateNotice;
import com.dominest.dominestbackend.domain.notice.datenotice.repository.DateNoticeRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DateNoticeService {
    private final DateNoticeRepository dateNoticeRepository;

    /*
    DATE별 로직 (yyyy-mm-dd 형식)
     */
    @Transactional
    public DateNotice createDateNotice(DateNoticeSaveRequest request) { // 알림 저장

        LocalTime time = LocalTime.parse(request.getTime());

        DateNotice dateNotice = DateNotice.builder()
                .date(request.getDate())
                .time(time)
                .alertBefore(request.getAlertBefore())
                .content(request.getContent())
                .apply(true)
                .build();

        return dateNoticeRepository.save(dateNotice);
    }

    // 알림 내보내기
    public List<String> getDateNoticeContent(String createBy, LocalDateTime requestTime) {
        List<String> contents = new ArrayList<>();

        // 유저가 생성한 알림 가져오기
        List<DateNotice> allDateNotices = dateNoticeRepository.findByCreatedBy(createBy);

        for (DateNotice dateNotice : allDateNotices) {

            LocalDateTime noticeTime = LocalDateTime.of(dateNotice.getDate(), dateNotice.getTime()); // 알림의 날짜+시간

            // 요청 시간과 알림 시간이 같거나 알림 시간에서 X분 전의 시간을 뺐을 때 요청 시간과 같다면
            if (noticeTime.minusMinutes(dateNotice.getAlertBefore()).isEqual(requestTime)
                    || noticeTime.isEqual(requestTime)) {

                contents.add(dateNotice.getContent());
            }
        }

        return contents;
    }

    // 알림 적용 여부 변경
    @Transactional
    public boolean switchDateApply(Long id) {
        DateNotice dateNotice = dateNoticeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
        return dateNotice.switchApply();
    }

    // 유저가 저장한 모든 알림 리스트
    @Transactional(readOnly = true)
    public List<DateNoticeResponse> getDateNoticesByUser(Principal principal) {
        return dateNoticeRepository.findByCreatedBy(principal.getName()).stream()
                .map(dateNotice -> DateNoticeResponse.of(
                        dateNotice.getId(),
                        dateNotice.getDate(),
                        dateNotice.getTime(),
                        dateNotice.getAlertBefore(),
                        dateNotice.getContent(),
                        dateNotice.isApply())
                )
                .collect(Collectors.toList());
    }
}
