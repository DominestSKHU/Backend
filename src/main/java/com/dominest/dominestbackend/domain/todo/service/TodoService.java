package com.dominest.dominestbackend.domain.todo.service;

import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.api.todo.request.TodoSaveRequest;
import com.dominest.dominestbackend.domain.todo.Todo;
import com.dominest.dominestbackend.domain.todo.repository.TodoRepository;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createTodo(TodoSaveRequest request, Principal principal){
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("유저 못 찾았음!"));

        try {
            Todo todo = Todo.builder()
                    .date(request.getDate())
                    .task(request.getTask())
                    .userName(user.getName())
                    .checkYn(false) // 기본적으로 false 처리
                    .build();

            todoRepository.save(todo);
        }
        catch (Exception e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void updateTodoCheckStatus(Long todoId, boolean checkYn) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new EntityNotFoundException(todoId + "에 해당하는 Todo가 없습니다."));

        todo.updateCheckYn(checkYn);
    }

    public List<Todo> getTodosByDateAndUnchecked(LocalDate date) {
        List<Todo> todosByDate = todoRepository.findByDate(date);
        List<Todo> uncheckedTodos = todoRepository.findByCheckYn(false);

        // 두 리스트를 합칩니다.
        List<Todo> result = new ArrayList<>();
        result.addAll(todosByDate);
        result.addAll(uncheckedTodos);

        return result.stream().distinct().collect(Collectors.toList());
    }
}
