package com.dominest.dominestbackend.domain.post.sanitationcheck.floor;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.sanitationcheck.SanitationCheckPost;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Floor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "floor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CheckedRoom> checkedRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sanitation_check_post_id", nullable = false)
    private SanitationCheckPost sanitationCheckPost;

    @Range(min = 2, max = 10, message = "층수는 2층 이상 10층 이하만 가능합니다")
    @Column(nullable = false)
    private Integer floorNumber;

    @Builder
    private Floor(Integer floorNumber, SanitationCheckPost sanitationCheckPost) {
        this.floorNumber = floorNumber;
        this.sanitationCheckPost = sanitationCheckPost;
    }
}














