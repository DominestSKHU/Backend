package com.dominest.dominestbackend.domain.post.sanitationcheck;

import com.dominest.dominestbackend.domain.post.common.Post;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.Floor;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SanitationCheckPost extends Post {
    @OneToMany(mappedBy = "sanitationCheckPost"
            , fetch = FetchType.LAZY)
    private List<Floor> floors;

    @Enumerated(EnumType.STRING)
    private ResidenceSemester residenceSemester;

    @Builder
    private SanitationCheckPost(String titleWithCurrentDate, User writer, Category category
            , ResidenceSemester residenceSemester
    ) {
        super(titleWithCurrentDate, writer, category);
        this.residenceSemester = residenceSemester;
    }
}
