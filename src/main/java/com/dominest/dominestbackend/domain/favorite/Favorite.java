package com.dominest.dominestbackend.domain.favorite;


import com.dominest.dominestbackend.domain.common.BaseEntity;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /*
    * 즐겨찾기 상태를 boolean으로 표현한다.
    * Favorite 객체가 있으면 TRUE, 없으면 FALSE 취급할 수도 있겠으나,
    * 잦은 즐겨찾기 추가/취소에 대한 DB 부하를 줄이기 위해 boolean으로 표현한다.
    * 이로써 FavoriteService.addOrUndo() 메서드에서 즐겨찾기 객체가 없으면 새로 생성하고, 있으면 상태를 바꿀 수 있다.
    * */
    @Column(nullable = false)
    private boolean onOff;  // 즐겨찾기 상태

    public boolean switchOnOff() {
        this.onOff = !this.onOff;
        return this.onOff;
    }

    @Builder
    public Favorite(Category category, User user) {
        this.category = category;
        this.user = user;
        this.onOff = true;
    }
}
