package com.dominest.dominestbackend.api.todo.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.todo.request.TodoSaveRequest;
import com.dominest.dominestbackend.api.todo.response.TodoUserResponse;
import com.dominest.dominestbackend.domain.todo.Todo;
import com.dominest.dominestbackend.domain.todo.repository.TodoRepository;
import com.dominest.dominestbackend.domain.todo.service.TodoService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;

    @PostMapping("/task") // 투두(할 일) 저장
    public RspTemplate<String> saveTodoList(@RequestBody @Valid TodoSaveRequest request, Principal principal){
        todoService.createTodo(request, principal);
        return new RspTemplate<>(HttpStatus.OK
                , "투두를 저장했습니다.", request.getTask());
    }

    @PutMapping("/{todoId}/check") // 투두 상태 업데이트
    public RspTemplate<String>updateTodoCheckStatus(@PathVariable Long todoId, @RequestBody @Valid BoolDto boolDto
    ) {
        todoService.updateTodoCheckStatus(todoId, boolDto.getCheckYn());
        return new RspTemplate<>(HttpStatus.OK
                , todoId + "번 Todo의 checkYn 상태를 " + boolDto.getCheckYn() + "로 업데이트했습니다.");
    }

    @NoArgsConstructor
    @Getter
    public static class BoolDto {
        @NotNull(message = "boolean 값이 null입니다")
        Boolean checkYn;
    }

    @GetMapping("/list") // 투두 리스트 가져오기
    public RspTemplate<List<Todo>> getTodos() {
        List<Todo> todos = todoService.getTodos();

        return new RspTemplate<>(HttpStatus.OK, "모든 투두를 성공적으로 불러왔습니다. ", todos);
    }

    @DeleteMapping("/{todoId}")
    public RspTemplate<Void> deleteEvaluation(@PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
        return new RspTemplate<>(HttpStatus.OK, todoId + "번 투두가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/user-name") // 투두 근로자 불러오기
    public RspTemplate<List<TodoUserResponse>> getUserInfo() {
        List<TodoUserResponse> nameResponse = todoService.getUserNameTodo();
        return new RspTemplate<>(HttpStatus.OK, "유저의 이름을 모두 불러오는데 성공했습니다.", nameResponse);
    }
}