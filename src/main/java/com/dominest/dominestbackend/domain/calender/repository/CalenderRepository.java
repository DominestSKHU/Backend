package com.dominest.dominestbackend.domain.calender.repository;

import com.dominest.dominestbackend.domain.calender.Calender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CalenderRepository extends JpaRepository<Calender, Long> {
    List<Calender> findByDate(LocalDate date);

    void deleteByDate(LocalDate date);

}
