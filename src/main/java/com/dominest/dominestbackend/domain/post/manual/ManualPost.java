package com.dominest.dominestbackend.domain.post.manual;

import com.dominest.dominestbackend.domain.post.common.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.user.User;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ManualPost extends Post {

    @Column(nullable = false)
    private String htmlContent;

    private boolean isModified;

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

}
