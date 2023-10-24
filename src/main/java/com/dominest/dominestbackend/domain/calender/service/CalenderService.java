package com.dominest.dominestbackend.domain.calender.service;

import com.dominest.dominestbackend.api.calendar.request.CalenderSaveRequest;
import com.dominest.dominestbackend.api.calendar.response.CalendarMonthResponse;
import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.domain.calender.Calender;
import com.dominest.dominestbackend.domain.calender.repository.CalenderRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
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

    public RspTemplate<List<Calender>> getByDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        List<Calender> events = calenderRepository.findByDate(date);

        return new RspTemplate<>(HttpStatus.OK, dateString +"의 일정을 성공적으로 가져왔습니다.", events);

        // 해당 날짜에 대한 일정이 없을 때는 빈 리스트 반환하도록 에러 처리 X
    }

    /*- 월별 일정 가져오기 -------*/
    public RspTemplate<List<CalendarMonthResponse>> getByMonth(String dateString) {
        YearMonth yearMonth = YearMonth.parse(dateString);
        int daysInMonth = yearMonth.lengthOfMonth();
        List<CalendarMonthResponse> events = new ArrayList<>();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), day);
            List<Calender> dailyEvents = calenderRepository.findByDate(date);
            boolean hasEvents = !dailyEvents.isEmpty();
            events.add(new CalendarMonthResponse(day, hasEvents));
        }

        return new RspTemplate<>(HttpStatus.OK, dateString +"의 일정을 성공적으로 가져왔습니다.", events);
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
