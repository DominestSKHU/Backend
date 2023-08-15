package com.dominest.dominestbackend.domain.post.image;

import com.dominest.dominestbackend.domain.post.common.Post;
import com.dominest.dominestbackend.domain.user.User;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ImageType extends Post {

    /* 이미지의 업데이트 시 일부만을 업데이트하도록 할까 했지만,
       이미지 순서 문제나 로직 간소화를 위해 전체 업데이트(Setter) 로 결정 */
    @Setter
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> imageUrls;

    @Builder
    private ImageType(String title, User writer, List<String> imageUrls) {
        super(title, writer);
        this.imageUrls = imageUrls;
    }
}



