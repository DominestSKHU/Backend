package com.dominest.dominestbackend.api.todo.controller;

import com.dominest.dominestbackend.api.todo.request.TodoSaveRequest;
import com.dominest.dominestbackend.domain.todo.Todo;
import com.dominest.dominestbackend.domain.todo.repository.TodoRepository;
import com.dominest.dominestbackend.domain.todo.service.TodoService;
import com.dominest.dominestbackend.domain.user.User;
import com.dominest.dominestbackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;

    private final TodoRepository todoRepository;

    private final UserRepository userRepository;

    // 투두 저장
    @PostMapping("/save")
    public ResponseEntity<Void> saveTodoList(@RequestBody @Valid TodoSaveRequest request, Principal principal){
        todoService.createTodo(request, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    // 현재 로그인한 유저의 투두 목록
    @GetMapping("/myTodo")
    public ResponseEntity<List<Todo>> getMyTodo(Principal principal) {
        String userEmail = principal.getName();
        Optional<User> user = userRepository.findByEmail(userEmail);

        List<Todo> myTodoList = todoRepository.findByUserName(user.get().getName());

        return ResponseEntity.status(HttpStatus.OK).body(myTodoList);
    }

    // 투두 상태 업데이트
    @PutMapping("/{todoId}/check")
    public ResponseEntity<String> updateTodoCheckStatus(@PathVariable Long todoId, @RequestParam("checkYn") boolean checkYn
    ) {
        todoService.updateTodoCheckStatus(todoId, checkYn);
        return ResponseEntity.ok(todoId + "번 Todo의 checkYn 상태를 " + checkYn + "로 업데이트했습니다.");
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getTodosByDateAndUnchecked(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Todo> todos = todoService.getTodosByDateAndUnchecked(date);
        return ResponseEntity.ok(todos);
    }
}