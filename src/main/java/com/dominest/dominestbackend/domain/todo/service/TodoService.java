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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            String username = parts[1];

            Todo todo = Todo.builder()
                    .date(LocalDateTime.now()) // 날짜
                    .task(request.getTask()) // 할 일
                    .requester(username) // 할 일을 부여하는 사람
                    .requestReceiver(request.getRequestReceiver()) // 할 일을 부여받은 (요청받은) 사람
                    .checkYn(false) // 기본적으로 false 처리
                    .build();

            todoRepository.save(todo);
        }
    }

    @Transactional
    public void updateTodoCheckStatus(Long todoId, boolean checkYn) { // checkYn 업데이트
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new EntityNotFoundException(todoId + "에 해당하는 Todo가 없습니다."));

        todo.updateCheckYn(checkYn);
    }

    public List<Todo> getTodos() { // 모든 투두리스트 불러오기
        List<Todo> todos = todoRepository.findAll();

        todos.sort((a, b) -> {
            if (Boolean.compare(a.isCheckYn(), b.isCheckYn()) == 0) {
                return Long.compare(b.getTodoId(), a.getTodoId());  // todoId 역순으로 정렬
            } else {
                return Boolean.compare(a.isCheckYn(), b.isCheckYn());  // 체크되지 않은 할 일이 먼저 오도록 정렬
            }
        });
        return todos;
    }

    @Transactional
    public void deleteTodo(Long todoId) { // 투두 삭제
        Optional<Todo> todo = todoRepository.findById(todoId);

        if (todo.isEmpty()) {
            throw new BusinessException(ErrorCode.TODO_NOT_FOUND);
        }

        todoRepository.delete(todo.get());

    }

    public List<TodoUserResponse> getUserNameTodo() { // 투두 근로자 선택
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new TodoUserResponse(user.getName()))
                .collect(Collectors.toList());
    }
}