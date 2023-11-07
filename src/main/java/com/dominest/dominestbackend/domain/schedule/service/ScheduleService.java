package com.dominest.dominestbackend.domain.schedule.service;

import com.dominest.dominestbackend.api.schedule.request.ScheduleDeleteRequest;
import com.dominest.dominestbackend.api.schedule.request.ScheduleSaveRequest;
import com.dominest.dominestbackend.api.schedule.response.ScheduleInfo;
import com.dominest.dominestbackend.api.schedule.response.TimeSlotInfo;
import com.dominest.dominestbackend.api.schedule.response.UserScheduleResponse;
import com.dominest.dominestbackend.domain.schedule.Schedule;
import com.dominest.dominestbackend.domain.schedule.repository.ScheduleRepository;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final UserRepository userRepository;

    @Transactional
    public void saveSchedule(ScheduleSaveRequest request) { // 스케줄 저장
        String username = request.getUsername();
        Schedule.Weekday dayOfWeek = Schedule.Weekday.fromString(request.getDayOfWeek()); // 문자열을 Weekday 열거형으로 변환
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();

        // 주어진 시간대를 1시간 단위로 나누어 처리
        for (LocalTime time = LocalTime.parse(startTime); time.isBefore(LocalTime.parse(endTime)); time = time.plusHours(1)) {
            String timeSlot = createTimeSlot(time);

            // 해당하는 스케쥴 찾아 유저 이름을 추가 및 저장, 없는 경우 새로 생성
            scheduleRepository.findByDayOfWeekAndTimeSlot(dayOfWeek, timeSlot) // dayOfWeek 타입 변경
                    .ifPresent(schedule -> {
                        schedule.getUsernames().add(username);
                        scheduleRepository.save(schedule);
                    });
        }
    }

    private String createTimeSlot(LocalTime time) {

        return time.toString() + " ~ " + time.plusHours(1).toString();
    }

    public List<ScheduleInfo> getSchedule() {
        Map<String, ScheduleInfo> scheduleInfoMap = new LinkedHashMap<>();

        Schedule.Weekday[] weekdays = Schedule.Weekday.values();

        // 빈 스케줄 정보를 요일 순서대로 초기화
        for (Schedule.Weekday dayOfWeek : weekdays) {
            scheduleInfoMap.put(dayOfWeek.name(), new ScheduleInfo(dayOfWeek.name(), new ArrayList<>()));
        }

        // DB에서 스케줄 정보를 가져옴 (요일과 시간 순으로 정렬)
        List<Schedule> schedules = scheduleRepository.findAll(Sort.by(Sort.Order.asc("dayOfWeek"), Sort.Order.asc("timeSlot")));

        // 가져온 스케줄 정보를 요일별로 분류
        for (Schedule schedule : schedules) {
            String dayOfWeek = schedule.getDayOfWeek().name();
            String timeSlot = schedule.getTimeSlot();
            List<String> usernames = schedule.getUsernames(); // 모든 사용자 이름 가져오기

            TimeSlotInfo timeSlotInfo = new TimeSlotInfo(timeSlot, usernames); // 사용자 이름 리스트를 TimeSlotInfo에 전달
            scheduleInfoMap.get(dayOfWeek).getTimeSlotInfos().add(timeSlotInfo);
        }

        // 요일 순서대로 결과 리스트를 생성
        List<ScheduleInfo> result = new ArrayList<>();

        for (Schedule.Weekday dayOfWeek : weekdays) {
            result.add(scheduleInfoMap.get(dayOfWeek.name()));
        }

        return result;
    }



    public List<UserScheduleResponse> getUserInfo(){ // 유저 이름, 폰번호 가져오기
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserScheduleResponse(user.getName(), user.getPhoneNumber()))
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteSchedule(ScheduleDeleteRequest request) {
        String username = request.getUsername();
        String dayOfWeek = request.getDayOfWeek();
        String timeSlot = request.getTimeSlot();

        // 요일과 시간대로 스케줄 조회
        Optional<Schedule> optionalSchedule = scheduleRepository.findByDayOfWeekAndTimeSlot(Schedule.Weekday.valueOf(dayOfWeek), timeSlot);
        if (!optionalSchedule.isPresent()) {  // 결과가 없다면
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        Schedule schedule = optionalSchedule.get();

        boolean removed = schedule.getUsernames().remove(username); // 스케줄에서 사용자 제거

        if (!removed) {  // 해당 사용자가 스케줄에 존재하지 않는다면
            throw new BusinessException(ErrorCode.USER_NOT_FOUND_IN_SCHEDULE);
        }
    }


}
