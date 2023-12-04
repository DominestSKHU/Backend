package com.dominest.dominestbackend.domain.todo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date; // 날짜

    @Column(nullable = false)
    private String task;// 할일 작성

    @Column(nullable = false)
    private String userName; // 요청 하는 사람

    private String receiveRequest; // 요청 받는 사람

    @Column(nullable = false)
    private boolean checkYn; // 투두 달성 true, false

    @Builder
    private Todo(LocalDateTime date, String task, String userName, String receiveRequest, boolean checkYn) {
        this.date = LocalDateTime.now();
        this.task = task;
        this.userName = userName;
        this.receiveRequest = receiveRequest;
        this.checkYn = checkYn;
    }

    public void updateCheckYn(boolean checkYn) {
        this.checkYn = checkYn;
    }

}