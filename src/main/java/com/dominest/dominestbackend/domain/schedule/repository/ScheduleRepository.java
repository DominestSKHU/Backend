package com.dominest.dominestbackend.domain.schedule.repository;

import com.dominest.dominestbackend.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDayOfWeekAndTimeSlot(String dayOfWeek, String timeSlot);

}
