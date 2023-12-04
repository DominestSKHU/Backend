package com.dominest.dominestbackend.api.todo.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TodoSaveRequest {

    @NotNull(message = "할 일을 입력해주세요.")
    private String task; // 할일

    private String receiveRequest; // 요청 받는 사람

}