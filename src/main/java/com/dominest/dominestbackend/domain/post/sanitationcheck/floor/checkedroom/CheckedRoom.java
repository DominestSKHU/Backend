package com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.Floor;
import com.dominest.dominestbackend.domain.resident.Resident;
import com.dominest.dominestbackend.domain.room.Room;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import javax.persistence.*;

/**
 * 아래의 사항들을 점검한다.
 * 1. 실내  2. 쓰레기방치  3. 화장실
 * 4. 샤워실 5. 보관금지
 * 6. 통과(ENUM)
 * 7. 비고 (String 자유입력)
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CheckedRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private Boolean indoor = false;
    private Boolean leavedTrash = false;
    private Boolean toilet = false;
    private Boolean shower = false;
    private Boolean prohibitedItem = false;

    @Enumerated(EnumType.STRING)
    private PassState passed;

    private String etc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = true)
    private Resident resident;

    @Builder
    private CheckedRoom(Room room, PassState passed
            , String etc, Floor floor, Resident resident) {
        this.room = room;
        this.indoor = false;
        this.leavedTrash = false;
        this.toilet = false;
        this.shower = false;
        this.prohibitedItem = false;
        this.passed = passed;
        this.etc = etc;
        this.floor = floor;
        this.resident = resident;
    }

    @RequiredArgsConstructor
    public enum PassState {
        NOT_PASSED("미통과"), FIRST_PASSED("1차 통과"), SECOND_PASSED("2차 통과")
        , THIRD_PASSED("3차 통과"), FOURTH_PASSED("4차 통과"), FIFTH_PASSED("5차 통과")
        , SIXTH_PASSED("6차 통과");

        @JsonValue
        private final String value;
    }
}














