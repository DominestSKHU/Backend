package com.dominest.dominestbackend.api.post.recent.dto;

import com.dominest.dominestbackend.api.common.AuditLog;
import com.dominest.dominestbackend.domain.post.common.RecentPost;
import com.dominest.dominestbackend.domain.post.component.category.component.Type;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecentPostListDto {
    @Getter
    public static class Res {
        List<RecentPostDto> recentPosts;

        public static Res from(Page<RecentPost> recentPosts) {
            List<RecentPostDto> recentPostDtos = RecentPostDto.from(recentPosts);
            return new Res(recentPostDtos);
        }

        public Res(List<RecentPostDto> recentPosts) {
            this.recentPosts = recentPosts;
        }
    }


    @Builder
    @Getter
    static class RecentPostDto {
        Long id;
        String title;
        String link;
        String categoryLink;
        Type categoryType;
        AuditLog auditLog;

        static RecentPostDto from(RecentPost recentPost) {
            return RecentPostDto.builder()
                    .id(recentPost.getId())
                    .title(recentPost.getTitle())
                    .link(recentPost.getLink())
                    .categoryLink(recentPost.getCategoryLink())
                    .categoryType(recentPost.getCategoryType())
                    .auditLog(AuditLog.from(recentPost))
                    .build();
        }

        static List<RecentPostDto> from(Page<RecentPost> recentPosts) {
            return recentPosts
                    .map(RecentPostDto::from)
                    .toList();
        }
    }




}
