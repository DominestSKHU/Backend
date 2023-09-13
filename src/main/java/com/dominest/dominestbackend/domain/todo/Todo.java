package com.dominest.dominestbackend.domain.todo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @Column(nullable = false)
    private LocalDate date; // 날짜

    @Column(nullable = false)
    private String task;// 할일 작성

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private boolean checkYn; // 투두 달성 true, false

    @Builder
    private Todo(LocalDate date, String task, String userName, boolean checkYn) {
        this.date = date;
        this.task = task;
        this.userName = userName;
        this.checkYn = checkYn;
    }

    public void updateCheckYn(boolean checkYn) {
        this.checkYn = checkYn;
    }

}