package com.dominest.dominestbackend.domain.room.roomhistory;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.room.Room;
import com.dominest.dominestbackend.global.validation.PhoneNumber;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 방 거주 내역을 기록한다.
 * 이 기록들은 하나의 Room을 참조한다.
 *
 * 방 거주 내역은 입사생이 입사할 때마다(엑셀 등록, 단건 등록과 수정) 기록된다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RoomHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String residentName;
    @Column(nullable = true)
    private LocalDate admissionDate;
    @Column(nullable = true)
    private LocalDate leavingDate;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Builder
    private RoomHistory(String residentName, LocalDate admissionDate, LocalDate leavingDate
            , String phoneNumber, String studentId, Room room) {
        this.residentName = residentName;
        this.admissionDate = admissionDate;
        this.leavingDate = leavingDate;
        this.phoneNumber = phoneNumber;
        this.studentId = studentId;
        this.room = room;
    }
}











