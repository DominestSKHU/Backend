package com.dominest.dominestbackend.api.todo.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.todo.request.TodoSaveRequest;
import com.dominest.dominestbackend.domain.todo.Todo;
import com.dominest.dominestbackend.domain.todo.repository.TodoRepository;
import com.dominest.dominestbackend.domain.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;

    private final TodoRepository todoRepository;

    // 투두 저장
    @PostMapping("/save")
    public RspTemplate<String> saveTodoList(@RequestBody @Valid TodoSaveRequest request, Principal principal){
        todoService.createTodo(request, principal);
        return new RspTemplate<>(HttpStatus.OK
                , "투두를 저장했습니다.", request.getTask() + ", " + request.getReceiveRequest());
    }

    // 투두 상태 업데이트
    @PutMapping("/{todoId}/check")
    public RspTemplate<String>updateTodoCheckStatus(@PathVariable Long todoId, @RequestParam("checkYn") boolean checkYn
    ) {
        todoService.updateTodoCheckStatus(todoId, checkYn);
        return new RspTemplate<>(HttpStatus.OK
                , todoId + "번 Todo의 checkYn 상태를 " + checkYn + "로 업데이트했습니다.");
    }

    @GetMapping("/list") // 투두리스트
    public List<Todo> getTodos() {
        List<Todo> todos = todoRepository.findAll();

        todos.sort((a, b) -> {
            if (Boolean.compare(a.isCheckYn(), b.isCheckYn()) == 0) {
                return Long.compare(b.getTodoId(), a.getTodoId());  // todoId 역순으로 정렬
            } else {
                return Boolean.compare(a.isCheckYn(), b.isCheckYn());  // 체크되지 않은게 먼저 오도록 정렬
            }
        });

        return todos;
    }

    @DeleteMapping("/delete/{todoId}")
    public RspTemplate<Void> deleteEvaluation(@PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
        return new RspTemplate<>(HttpStatus.OK, todoId + "번 투두가 성공적으로 삭제되었습니다.");
    }
}