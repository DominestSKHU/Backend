package com.dominest.dominestbackend.domain.calendar.service;

import com.dominest.dominestbackend.api.calendar.request.CalendarSaveRequest;
import com.dominest.dominestbackend.api.calendar.response.CalendarMonthResponse;
import com.dominest.dominestbackend.domain.calendar.Calendar;
import com.dominest.dominestbackend.domain.calendar.repository.CalendarRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {
    private final CalendarRepository calendarRepository;

    @Transactional
    public void addCalendar(CalendarSaveRequest request) { // 캘린더에 일정 추가
        Calendar calender = Calendar.builder()
                .date(request.getDate())
                .content(request.getContent())
                .build();
        calendarRepository.save(calender);
    }

    public List<Calendar> getByDate(String dateString) { // 해당 날짜에 해당하는 일정 가져오기 (ex. 년-월-일 )

        // 해당 날짜에 대한 일정이 없을 때는 빈 리스트 반환하도록 에러 처리 X
        return calendarRepository.findByDate(LocalDate.parse(dateString));

    }

    public List<CalendarMonthResponse> getByMonth(String dateString) { // 해당 월에 해당하는 일정 가져오기 (ex. 년-월 )
        YearMonth yearMonth = YearMonth.parse(dateString);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        // 해당 월의 모든 일정 가져오기
        List<Calendar> monthEvents = calendarRepository.findByDateBetween(startOfMonth, endOfMonth);

        // 일정 있는 날짜 저장
        Set<LocalDate> eventDates = monthEvents.stream().map(Calendar::getDate).collect(Collectors.toSet());

        // 1일 ~ 해당 월의 마지막 날까지 스트림 생성 -> CalendarMonthResponse로 변환
        List<CalendarMonthResponse> events = IntStream.rangeClosed(1, yearMonth.lengthOfMonth())
                .mapToObj(day -> LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), day))
                .map(date -> new CalendarMonthResponse(date.getDayOfMonth(), eventDates.contains(date)))
                .collect(Collectors.toList());

        return events;

        // 해당 월에 대한 일정이 없을 때는 빈 리스트 반환하도록 에러 처리 X
    }


    @Transactional
    public void deleteEventsByDate(String dateString) { // 해당 날짜에 대한 일정 삭제
        LocalDate date = LocalDate.parse(dateString);
        List<Calendar> events = calendarRepository.findByDate(date);

        // 해당 날짜의 일정이 없다면
        if(events.isEmpty()) {
            throw new BusinessException(ErrorCode.CALENDAR_NOT_FOUND);
        }

        calendarRepository.deleteByDate(date);
    }
}
