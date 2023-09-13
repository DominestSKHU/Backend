package com.dominest.dominestbackend.api.todo.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TodoSaveRequest {
    private LocalDate date; // 날짜

    @NotNull(message = "할 일을 입력해주세요.")
    private String task; // 할일
}