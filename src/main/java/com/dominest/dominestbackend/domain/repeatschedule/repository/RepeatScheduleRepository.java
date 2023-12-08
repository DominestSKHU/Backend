package com.dominest.dominestbackend.domain.repeatschedule.repository;

import com.dominest.dominestbackend.domain.repeatschedule.RepeatSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepeatScheduleRepository extends JpaRepository<RepeatSchedule, Long> {
}
