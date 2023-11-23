package com.dominest.dominestbackend.domain.post.manual;

import com.dominest.dominestbackend.domain.post.common.Post;
import lombok.*;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.user.User;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ManualPost extends Post {
    @Column(nullable = false)
    private String htmlContent;

    private boolean isModified;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> attachmentUrls;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> imageUrls;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> videoUrls;

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

    public void setAttachmentNames(Set<String> attachmentUrls, Set<String> imageUrls,
                                   Set<String> videoUrls) {
        this.attachmentUrls = attachmentUrls;
        this.imageUrls = imageUrls;
        this.videoUrls = videoUrls;
    }

    public void addAttachmentUrls(Set<String> attachmentUrls) {
        this.attachmentUrls.addAll(attachmentUrls);
    }

    public void addImageUrls(Set<String> imageUrls) {
        this.imageUrls.addAll(imageUrls);
    }

    public void addVideoUrls(Set<String> videoUrls) {
        this.videoUrls.addAll(videoUrls);
    }

    public void deleteUrls(Set<String> toDeleteAttachmentUrls, Set<String> toDeleteImageUrls, Set<String> toDeleteVideoUrls) {

        if(toDeleteAttachmentUrls != null)
            this.attachmentUrls.removeAll(toDeleteAttachmentUrls);
        if(toDeleteImageUrls != null)
            this.imageUrls.removeAll(toDeleteImageUrls);
        if(toDeleteVideoUrls != null)
            this.videoUrls.removeAll(toDeleteVideoUrls);
    }
}