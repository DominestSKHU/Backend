package com.dominest.dominestbackend.domain.resident.penalty;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import com.dominest.dominestbackend.domain.resident.Resident;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** CheckRoom과 Resident의 양방향 매핑 테이블로, 벌점관리를 위함 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PenaltyHist extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_room_id", nullable = false)
    private CheckedRoom checkedRoom;

    @Column(nullable = false)
    private Integer penalty;

    @Builder
    private PenaltyHist(Resident resident, CheckedRoom checkedRoom, Integer penalty) {
        this.resident = resident;
        this.checkedRoom = checkedRoom;
        this.penalty = penalty;
    }



    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }
}




















