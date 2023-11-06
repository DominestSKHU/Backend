package com.dominest.dominestbackend.domain.dar;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Dar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date; // 날짜

    @Column(nullable = false)
    private String content; // 내용 작성

    @Builder
    private Dar(LocalDate date, String content) {
        this.date = date;
        this.content = content;
    }
}