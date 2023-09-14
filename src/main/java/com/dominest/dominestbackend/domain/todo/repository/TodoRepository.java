package com.dominest.dominestbackend.domain.todo.repository;

import com.dominest.dominestbackend.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByDate(LocalDateTime date);

    List<Todo> findByCheckYn(boolean checkYn);


}