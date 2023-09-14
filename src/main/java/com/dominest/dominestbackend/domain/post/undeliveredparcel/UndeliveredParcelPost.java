package com.dominest.dominestbackend.domain.post.undeliveredparcel;

import com.dominest.dominestbackend.domain.post.common.Post;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.component.UndeliveredParcel;
import com.dominest.dominestbackend.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * 장기미수령 택배 관리대장 게시글
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UndeliveredParcelPost extends Post {
    @OneToMany(targetEntity = UndeliveredParcel.class, mappedBy = "post"
            , fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<UndeliveredParcel> undelivParcels = new ArrayList<>();

    public void setTitle(String title) {
        super.setTitle(title);
    }
    @Builder
    private UndeliveredParcelPost(String titleWithCurrentDate, User writer, Category category) {
        // currentDate는 "yyyy-MM-dd 장기미수령 택배" 형식의 문자열이다.
        super(titleWithCurrentDate, writer, category);
    }
}



