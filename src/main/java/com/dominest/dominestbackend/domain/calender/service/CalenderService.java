package com.dominest.dominestbackend.domain.calender.service;

import com.dominest.dominestbackend.api.calendar.request.CalenderSaveRequest;
import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.domain.calender.Calender;
import com.dominest.dominestbackend.domain.calender.repository.CalenderRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalenderService {
    private final CalenderRepository calenderRepository;

    @Transactional
    public RspTemplate<String> addCalender(CalenderSaveRequest request) {
        try {
            Calender calender = Calender.builder()
                    .date(request.getDate())
                    .content(request.getContent())
                    .build();
            calenderRepository.save(calender);
            return new RspTemplate<>(HttpStatus.OK
                    , "일정을 저장했습니다.", request.getDate() + " 날짜의 [" + request.getContent() + "] 내용을 저장하였습니다.");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Calender> getByDate(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            List<Calender> events = calenderRepository.findByDate(date);
            if(events.isEmpty()){
                throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
            }

            return events;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
        }
    }

    @Transactional
    public RspTemplate<String> deleteEventsByDate(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            calenderRepository.deleteByDate(date);
            return new RspTemplate<>(HttpStatus.OK
                    , "삭제 완료!!!", dateString + " 날짜의 일정을 삭제하였습니다.");
        } catch (Exception e) {
            // 예외 처리 로직 추가
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
