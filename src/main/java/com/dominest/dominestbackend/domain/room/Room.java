package com.dominest.dominestbackend.domain.room;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.resident.Resident;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 *  기숙사 호실 정보. 행기 기숙사 데이터.
 *
 *  엑셀 데이터는 roomNo, dormitory, assignedRoom이며
 *  나머지 속성들은 개발 편의를 위하여 추가함.
 *
 *  아래 2가지의 데이터는 잘 쓰지 않는다.
 *  1. 호실 roomNumber(1(미가엘), 2(행기))
 *  2. ,기숙사 dormitory (A(미) B(행)),
 *
 *  아래 데이터를 주로 다루게 될 듯.
 *  1. 배정방 assignedRoom
 */
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
    private Integer floorNo; // 층수

    // 한 방에 한 입사생만 입주할 수 있지만, 입사생은 여러 차수로 나뉘어져 있으므로 OneToMany
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Resident> residents;

    /** 아래의 roomNo, dormitory 는 미가엘, 행기를 구분하기 위해 사용된다. 비즈니스 로직상에서는 아직 쓸 일이 없음*/
    @Column(nullable = false)
    private Integer roomNo; // 호실. (1(미가엘), 2(행기))
    @Column(nullable = false)
    private String dormitory; // (A(미가엘) B(행기))

    @Builder
    private Room(String assignedRoom, Integer floorNo, Integer roomNo, String dormitory) {
        this.assignedRoom = assignedRoom;
        this.floorNo = floorNo;
        this.roomNo = roomNo;
        this.dormitory = dormitory;
    }
}





























