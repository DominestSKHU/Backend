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
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SanitationCheckPost extends Post {
    @OneToMany(mappedBy = "sanitationCheckPost"
            , fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Floor> floors;

    @Enumerated(EnumType.STRING)
    private ResidenceSemester residenceSemester;

    @Builder
    private SanitationCheckPost(User writer, Category category
            , ResidenceSemester residenceSemester
    ) {
        super(createTitle(), writer, category);
        this.residenceSemester = residenceSemester;
    }
    // 객체의 context를 전혀 반영하지 않으므로 static
    private static String createTitle() {
        // 원하는 형식의 문자열로 변환
        LocalDateTime now = LocalDateTime.now();
        return now.getYear() +
                "년 " +
                now.getMonthValue() +
                "월 방역호실점검";
    }
}
