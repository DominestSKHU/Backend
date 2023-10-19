package com.dominest.dominestbackend.domain.schedule.service;

import com.dominest.dominestbackend.api.schedule.request.ScheduleSaveRequest;
import com.dominest.dominestbackend.api.schedule.response.UserScheduleResponse;
import com.dominest.dominestbackend.domain.schedule.Schedule;
import com.dominest.dominestbackend.domain.schedule.repository.ScheduleRepository;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final UserRepository userRepository;

    @Transactional
    public void saveSchedule(ScheduleSaveRequest requests) {
        List<String> usernames = requests.getUsernames();

        try{
            Schedule schedule = scheduleRepository.findByDayOfWeekAndTimeSlot(requests.getDayOfWeek(), requests.getTimeSlot()).get(0);

            schedule.getUsernames().addAll(usernames);

            scheduleRepository.save(schedule);
        } catch (Exception e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Map<String, Object>> getSchedule() {
        List<Map<String, Object>> scheduleInfo = new ArrayList<>();

        List<String> daysOfWeek = Arrays.asList("월요일", "화요일", "수요일", "목요일", "금요일");
        List<String> timeSlots = Arrays.asList(
                "09:00 ~ 10:00", "10:00 ~ 11:00", "11:00 ~ 12:00", "12:00 ~ 13:00",
                "13:00 ~ 14:00", "14:00 ~ 15:00", "15:00 ~ 16:00", "16:00 ~ 17:00"
        );

        for (String dayOfWeek : daysOfWeek) {
            Map<String, Object> dayInfo = new HashMap<>();
            dayInfo.put("dayOfWeek", dayOfWeek); // 요일 이름 추가

            List<Map<String, Object>> timeSlotInfo = new ArrayList<>();
            for (String timeSlot : timeSlots) {
                Map<String, Object> slotInfo = new HashMap<>();
                // 시간대 정보 추가
                slotInfo.put("timeSlot", timeSlot);

                // 해당 요일과 시간대에 저장된 유저 이름 정보 조회
                List<String> usernames = getUsernamesByDayOfWeekAndTimeSlot(dayOfWeek, timeSlot);

                // 유저 이름 정보 추가
                slotInfo.put("usernames", usernames);

                timeSlotInfo.add(slotInfo);
            }

            dayInfo.put("timeSlots", timeSlotInfo);
            scheduleInfo.add(dayInfo);
        }
        return scheduleInfo;
    }

    public List<String> getUsernamesByDayOfWeekAndTimeSlot(String dayOfWeek, String timeSlot) {
        List<Schedule> schedules = scheduleRepository.findByDayOfWeekAndTimeSlot(dayOfWeek, timeSlot);

        List<String> usernames = new ArrayList<>();
        for (Schedule schedule : schedules) {
            usernames.addAll(schedule.getUsernames());
        }

        return usernames;
    }

    // 유저 이름, 폰번호 가져오기
    public List<UserScheduleResponse> getUserInfo(){
        List<User> user = userRepository.findAll();
        List<UserScheduleResponse> responses = new ArrayList<>();

        for(User user1 : user){
            UserScheduleResponse userScheduleResponse = new UserScheduleResponse(user1.getName(), user1.getPhoneNumber());
            responses.add(userScheduleResponse);
        }

        return responses;
    }
}
