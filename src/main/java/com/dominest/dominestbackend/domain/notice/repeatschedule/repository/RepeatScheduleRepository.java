package com.dominest.dominestbackend.domain.notice.repeatschedule.repository;

import com.dominest.dominestbackend.domain.notice.repeatschedule.RepeatSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepeatScheduleRepository extends JpaRepository<RepeatSchedule, Long> {
}
