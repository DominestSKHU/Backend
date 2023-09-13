package com.dominest.dominestbackend.domain.post.sanitationcheck.floor;

import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.sanitationcheck.SanitationCheckPost;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Floor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "floor", fetch = FetchType.LAZY)
    private List<CheckedRoom> checkedRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sanitation_check_post_id", nullable = false)
    private SanitationCheckPost sanitationCheckPost;

    @Builder
    private Floor(SanitationCheckPost sanitationCheckPost) {
        this.sanitationCheckPost = sanitationCheckPost;
    }
}














