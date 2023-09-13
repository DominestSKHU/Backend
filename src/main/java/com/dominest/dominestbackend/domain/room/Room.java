package com.dominest.dominestbackend.domain.room;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.resident.Resident;
import lombok.*;

import javax.persistence.*;

/**
 *  기숙사 호실 정보. 행기 기숙사 데이터.
 *
 *  아래 2가지의 데이터는 잘 쓰지 않는다.
 *  1. 호실 roomNumber(1(미가엘), 2(행기))
 *  2. ,기숙사 dormitory (A(미) B(행)),
 *
 *  아래 데이터를 주로 다루게 될 듯.
 *  1. 배정방 assignedRoom
 */
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Unique 할 수밖에 없음.
    private String assignedRoom; // 배정방. 'B1049A' 와 같음

    @Column(nullable = false)
    private Integer roomNumber; // 호실. (1(미가엘), 2(행기))
    @Column(nullable = false)
    private String dormitory; // (A(미가엘) B(행기))

    @OneToOne(mappedBy = "room", fetch = FetchType.LAZY)
    private Resident resident;

    @Builder
    private Room(Integer roomNumber, String assignedRoom, String dormitory) {
        this.roomNumber = roomNumber;
        this.assignedRoom = assignedRoom;
        this.dormitory = dormitory;
    }
}





























