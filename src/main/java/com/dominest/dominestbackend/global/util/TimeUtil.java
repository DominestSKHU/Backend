package com.dominest.dominestbackend.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class TimeUtil {
    /**
     *  작성 시점 시간 230715.
     *  000101 -> 2000-01-01
     *  240101 -> 1924-01-01
     *  800101 -> 1980-01-01
     *  990101 -> 1999-01-01
     */
    public static LocalDate parseyyMMddToLocalDate(String yyMMdd) {
        LocalDate localDate = LocalDate.parse(yyMMdd, DateTimeFormatter.ofPattern("yyMMdd"));
        int year = localDate.getYear();

        // 현재 연도보다 미래인 경우 100년 전으로 설정
        if (year > LocalDate.now().getYear()) {
            localDate = localDate.minusYears(100);
        }
        return localDate;
    }

    public static LocalDate parseyyMMddToLocalDate(LocalDate yyMMdd) {
        LocalDate copyedLocalDate = LocalDate.of(yyMMdd.getYear(), yyMMdd.getMonth(), yyMMdd.getDayOfMonth());
        int year = copyedLocalDate.getYear();

        // 현재 연도보다 미래인 경우 100년 전으로 설정
        if (year > LocalDate.now().getYear()) {
            copyedLocalDate = copyedLocalDate.minusYears(100);
        }
        return copyedLocalDate;
    }
}
