package com.dominest.dominestbackend.domain.dar.repository;

import com.dominest.dominestbackend.domain.dar.Dar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DarRepository extends JpaRepository<Dar, Long> {
    List<Dar> findByDate(LocalDate date);

    void deleteByDate(LocalDate date);

    List<Dar> findByDateBetween(LocalDate start, LocalDate end);


}
