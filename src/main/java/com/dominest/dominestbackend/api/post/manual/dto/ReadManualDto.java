package com.dominest.dominestbackend.api.post.manual.dto;

import com.dominest.dominestbackend.api.common.AuditLog;
import com.dominest.dominestbackend.domain.post.manual.ManualPost;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadManualDto {
    private static final String MANUAL_POST_LIST_URL_PREFIX = "/categories/";
    private static final String MANUAL_POST_LIST_URL_SUFFIX = "/posts/manual";

    @Getter
    @AllArgsConstructor
    public static class Res {
        ManualPostDto post;
        String categoryLink;
        int page;
        public static Res from(ManualPost manualPost, int page){
            ManualPostDto post = ManualPostDto.from(manualPost);
            Long categoryId = manualPost.getCategory().getId();
            String categoryLink = MANUAL_POST_LIST_URL_PREFIX + categoryId +
                    MANUAL_POST_LIST_URL_SUFFIX;
            return new Res(post, categoryLink, page);
        }

        Res(ManualPostDto post) {
            this.post = post;
        }

        @Builder
        @Getter
        static class ManualPostDto {
            long id;
            String title;
            String writerName;
            String htmlContent;
            LocalDateTime createTime;

            AuditLog auditLog;
            Set<String> attachmentUrls;
            Set<String> imageUrls;
            Set<String> videoUrls;

            static ManualPostDto from(ManualPost post){
                return ReadManualDto.Res.ManualPostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .writerName(post.getWriter().getName())
                        .htmlContent(post.getHtmlContent())
                        .createTime(post.getCreateTime())
                        .auditLog(AuditLog.from(post))
                        .attachmentUrls(post.getAttachmentUrls())
                        .imageUrls(post.getImageUrls())
                        .videoUrls(post.getVideoUrls())
                        .build();
            }
        }
    }
}
