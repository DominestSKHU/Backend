package com.dominest.dominestbackend.domain.schedule;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum Weekday {
        월요일, 화요일, 수요일, 목요일, 금요일;

        public static Weekday fromString(String value) {
            for (Weekday weekday : Weekday.values()) {
                if (weekday.name().equals(value)) {
                    return weekday;
                }
            }
            throw new IllegalArgumentException("Invalid weekday value: " + value);
        }
    }

    @Enumerated(EnumType.STRING)
    private Weekday dayOfWeek; // 요일

    private String timeSlot;  // 시간

    @ElementCollection
    @CollectionTable(name = "Schedule_Usernames", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "username")
    private List<String> usernames;

}