package com.dominest.dominestbackend.domain.repeatSchedule.service;

import com.dominest.dominestbackend.api.repeatNotice.response.RepeatNoticeSaveResponse;
import com.dominest.dominestbackend.api.repeatSchedule.request.RepeatScheduleSaveRequest;
import com.dominest.dominestbackend.api.repeatSchedule.resopnse.AllRepeatScheduleResponse;
import com.dominest.dominestbackend.api.repeatSchedule.resopnse.RepeatScheduleResponse;
import com.dominest.dominestbackend.domain.repeatNotice.RepeatNotice;
import com.dominest.dominestbackend.domain.repeatNotice.repository.RepeatNoticeRepository;
import com.dominest.dominestbackend.domain.repeatSchedule.RepeatSchedule;
import com.dominest.dominestbackend.domain.repeatSchedule.repository.RepeatScheduleRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepeatScheduleService {

    private final RepeatScheduleRepository repeatScheduleRepository;

    private final RepeatNoticeRepository repeatNoticeRepository;

    @Transactional // 반복일정 글 저장
    public RepeatScheduleResponse createDayNotices(RepeatScheduleSaveRequest request) {
        List<RepeatNotice> repeatNotices = findRepeatNotices(request.getRepeatNoticeIds());

        RepeatSchedule repeatSchedule = createRepeatSchedule(request, repeatNotices);

        RepeatSchedule savedRepeatedSchedule = repeatScheduleRepository.save(repeatSchedule);

        return createResponse(savedRepeatedSchedule, repeatNotices);
    }

    private List<RepeatNotice> findRepeatNotices(List<Long> repeatNoticeIds) { // RepeatNoticeIds 이용해 RepeatNotice 조회
        return repeatNoticeIds.stream()
                .map(repeatNoticeRepository::findById)
                .map(optionalRepeatNotice -> optionalRepeatNotice
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND)))
                .collect(Collectors.toList());
    }

    // RepeatScheduleSaveRequest + RepeatNotice 리스트 이용 => RepeatSchedule을 생성
    private RepeatSchedule createRepeatSchedule(RepeatScheduleSaveRequest request, List<RepeatNotice> repeatNotices) {
        RepeatSchedule repeatSchedule = RepeatSchedule.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .repeatNotices(new ArrayList<>())
                .build();

        repeatNotices.forEach(repeatSchedule::addRepeatNotice);

        return repeatSchedule;
    }

    // RepeatSchedule + RepeatNotice 리스트 이용 => 응답 생성
    private RepeatScheduleResponse createResponse(RepeatSchedule savedRepeatedSchedule, List<RepeatNotice> repeatNotices) {
        List<RepeatNoticeSaveResponse> repeatNoticeResponses = repeatNotices.stream()
                .map(repeatNotice -> RepeatNoticeSaveResponse.of(
                        repeatNotice.getId(),
                        repeatNotice.getDay(),
                        repeatNotice.getTime(),
                        repeatNotice.getAlertBefore(),
                        repeatNotice.getContent(),
                        repeatNotice.isApply(),
                        repeatNotice.getCreatedBy(),
                        repeatNotice.getCreateTime()
                ))
                .collect(Collectors.toList());

        return RepeatScheduleResponse.of(
                savedRepeatedSchedule.getId(),
                savedRepeatedSchedule.getTitle(),
                savedRepeatedSchedule.getDescription(),
                savedRepeatedSchedule.getCreatedBy(),
                savedRepeatedSchedule.getCreateTime(),
                repeatNoticeResponses
        );
    }

    public List<AllRepeatScheduleResponse> getAllRepeatSchedules() { // 모든 반복일정 글 조회
        return repeatScheduleRepository.findAll().stream()
                .map(repeatSchedule -> AllRepeatScheduleResponse.of(
                        repeatSchedule.getId(),
                        repeatSchedule.getTitle(),
                        repeatSchedule.getCreatedBy(),
                        repeatSchedule.getCreateTime()
                ))
                .collect(Collectors.toList());
    }


    public RepeatScheduleResponse getRepeatScheduleById(Long id) { // 해당 반복일정 글 상세조회
        RepeatSchedule repeatSchedule = repeatScheduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));

        List<RepeatNotice> repeatNotices = repeatNoticeRepository.findAllByRepeatSchedule(repeatSchedule);

        return createResponse(repeatSchedule, repeatNotices);
    }

}
