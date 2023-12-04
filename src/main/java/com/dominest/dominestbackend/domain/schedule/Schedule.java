package com.dominest.dominestbackend.domain.schedule;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek; // 요일

    @Enumerated(EnumType.STRING)
    private TimeSlot timeSlot;  // 시간

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "username")
    private List<String> usernames;

    private Schedule(DayOfWeek dayOfWeek, TimeSlot timeSlot) {
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
    }

    public static Schedule of(DayOfWeek dayOfWeek, TimeSlot timeSlot) {
        return new Schedule(dayOfWeek, timeSlot);
    }

    @RequiredArgsConstructor
    public enum DayOfWeek {
        MON("월요일"), TUE("화요일"), WED("수요일"), THU("목요일"), FRI("금요일");

        @JsonValue
        public final String value;

        public static DayOfWeek fromString(String value) {
            return Arrays.stream(DayOfWeek.values())
                    .filter(dayOfWeek -> dayOfWeek.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid weekday value: " + value));
        }
    }

    @RequiredArgsConstructor
    public enum TimeSlot {
        NINE_TO_TEN("09:00 ~ 10:00"), TEN_TO_ELEVEN("10:00 ~ 11:00"), ELEVEN_TO_TWELVE("11:00 ~ 12:00"),
        TWELVE_TO_ONE("12:00 ~ 13:00"), ONE_TO_TWO("13:00 ~ 14:00"), TWO_TO_THREE("14:00 ~ 15:00"),
        THREE_TO_FOUR("15:00 ~ 16:00"), FOUR_TO_FIVE("16:00 ~ 17:00");

        @JsonValue
        public final String value;

        public static TimeSlot fromString(String value) {
            return Arrays.stream(TimeSlot.values())
                    .filter(timeSlot -> timeSlot.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid timeSlot value: " + value));
        }
    }

}