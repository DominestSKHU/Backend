package com.dominest.dominestbackend.domain.todo.repository;

import com.dominest.dominestbackend.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserName(String userName);
    List<Todo> findByDate(LocalDate date);

    List<Todo> findByCheckYn(boolean checkYn);


}