package com.dominest.dominestbackend.domain.repeatSchedule.repository;

import com.dominest.dominestbackend.domain.repeatSchedule.RepeatSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepeatScheduleRepository extends JpaRepository<RepeatSchedule, Long> {
}
