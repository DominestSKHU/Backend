package com.dominest.dominestbackend.domain.dar.service;

import com.dominest.dominestbackend.api.dar.request.DarSaveRequest;
import com.dominest.dominestbackend.api.dar.response.DarMonthResponse;
import com.dominest.dominestbackend.domain.dar.Dar;
import com.dominest.dominestbackend.domain.dar.repository.DarRepository;
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
public class DarService {
    private final DarRepository darRepository;

    @Transactional
    public void addDar(DarSaveRequest request) { // 캘린더에 일정 추가
        Dar calender = Dar.builder()
                .date(request.getDate())
                .content(request.getContent())
                .build();
        darRepository.save(calender);
    }

    public List<Dar> getByDate(String dateString) { // 해당 날짜에 해당하는 일정 가져오기 (ex. 년-월-일 )

        // 해당 날짜에 대한 일정이 없을 때는 빈 리스트 반환하도록 에러 처리 X
        return darRepository.findByDate(LocalDate.parse(dateString));

    }

    public List<DarMonthResponse> getByMonth(String dateString) { // 해당 월에 해당하는 일정 가져오기 (ex. 년-월 )
        YearMonth yearMonth = YearMonth.parse(dateString);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        // 해당 월의 모든 일정 가져오기
        List<Dar> monthEvents = darRepository.findByDateBetween(startOfMonth, endOfMonth);

        // 일정 있는 날짜 저장
        Set<LocalDate> eventDates = monthEvents.stream().map(Dar::getDate).collect(Collectors.toSet());

        // 1일 ~ 해당 월의 마지막 날까지 스트림 생성 -> CalendarMonthResponse로 변환
        List<DarMonthResponse> events = IntStream.rangeClosed(1, yearMonth.lengthOfMonth())
                .mapToObj(day -> LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), day))
                .map(date -> new DarMonthResponse(date.getDayOfMonth(), eventDates.contains(date)))
                .collect(Collectors.toList());

        return events;

        // 해당 월에 대한 일정이 없을 때는 빈 리스트 반환하도록 에러 처리 X
    }


    @Transactional
    public void deleteEventsByDate(String dateString) { // 해당 날짜에 대한 일정 삭제
        LocalDate date = LocalDate.parse(dateString);
        List<Dar> events = darRepository.findByDate(date);

        // 해당 날짜의 일정이 없다면
        if(events.isEmpty()) {
            throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
        }

        darRepository.deleteByDate(date);
    }
}
