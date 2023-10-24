package com.dominest.dominestbackend.domain.todo.service;

import com.dominest.dominestbackend.api.todo.request.TodoSaveRequest;
import com.dominest.dominestbackend.api.todo.response.TodoUserResponse;
import com.dominest.dominestbackend.domain.todo.Todo;
import com.dominest.dominestbackend.domain.todo.repository.TodoRepository;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {
    private final TodoRepository todoRepository;

    private final UserRepository userRepository;

    @Transactional
    public void createTodo(TodoSaveRequest request, Principal principal){ // 투두 저장
        String[] parts = principal.getName().split(":");
        if (parts.length > 1) {
            String desiredValue = parts[1];

            try {
                Todo todo = Todo.builder()
                        .date(LocalDateTime.now())
                        .task(request.getTask())
                        .userName(desiredValue)
                        .receiveRequest(request.getReceiveRequest())
                        .checkYn(false) // 기본적으로 false 처리
                        .build();

                todoRepository.save(todo);
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Transactional
    public void updateTodoCheckStatus(Long todoId, boolean checkYn) { // checkYn 업데이트
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new EntityNotFoundException(todoId + "에 해당하는 Todo가 없습니다."));

        todo.updateCheckYn(checkYn);
    }

    @Transactional
    public void deleteTodo(Long todoId) {
        Optional<Todo> todo = todoRepository.findById(todoId);

        if (todo.isEmpty()) {
            throw new BusinessException(ErrorCode.TODO_NOT_FOUND);
        }

        todoRepository.delete(todo.get());

    }

    public List<TodoUserResponse> getUserNameTodo(){ // 투두 근로자 선택
        List<User> user = userRepository.findAll();
        List<TodoUserResponse> responses = new ArrayList<>();

        for(User user1 : user){
            TodoUserResponse userScheduleResponse = new TodoUserResponse(user1.getName());
            responses.add(userScheduleResponse);
        }

        return responses;
    }
}