package com.dominest.dominestbackend.domain.schedule;

import lombok.AccessLevel;
import lombok.Builder;
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
    private Long ScheduleId;

    private String dayOfWeek; // 요일

    private String timeSlot;  // 시간

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Schedule_Usernames", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "username")
    private List<String> usernames;


    @Builder
    public Schedule(String dayOfWeek, String timeSlot, List<String> usernames){
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
        this.usernames = usernames;
    }


}