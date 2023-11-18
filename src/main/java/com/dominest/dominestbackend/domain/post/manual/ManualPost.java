package com.dominest.dominestbackend.domain.post.manual;

import com.dominest.dominestbackend.domain.post.common.Post;
import lombok.*;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.user.User;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ManualPost extends Post {
    @Column(nullable = false)
    private String htmlContent;

    private boolean isModified;

    @Setter
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> attachmentUrls;

    @Setter
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> imageUrls;

    @Setter
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> videoUrls;

    @Builder
    private ManualPost(String title, User writer, Category category, String htmlContent) {
        super(title, writer, category);
        this.htmlContent = htmlContent;
        isModified = false;
    }

    @Builder
    private ManualPost(String title, User writer, Category category, String htmlContent, boolean isModified) {
        super(title, writer, category);
        this.htmlContent = htmlContent;
        this.isModified = isModified;
    }

    public void setUrls(List<String> attachmentUrls, List<String> imageUrls,
                   List<String> videoUrls) {
        this.attachmentUrls = attachmentUrls;
        this.imageUrls = imageUrls;
        this.videoUrls = videoUrls;
    }

}
