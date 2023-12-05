package com.dominest.dominestbackend.domain.schedule.repository;

import com.dominest.dominestbackend.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByDayOfWeekAndTimeSlot(Schedule.DayOfWeek dayOfWeek, Schedule.TimeSlot timeSlot);

}
