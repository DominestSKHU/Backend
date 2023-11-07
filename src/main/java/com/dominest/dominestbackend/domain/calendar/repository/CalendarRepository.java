package com.dominest.dominestbackend.domain.calendar.repository;

import com.dominest.dominestbackend.domain.calendar.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByDate(LocalDate date);

    void deleteByDate(LocalDate date);

    List<Calendar> findByDateBetween(LocalDate start, LocalDate end);


}
