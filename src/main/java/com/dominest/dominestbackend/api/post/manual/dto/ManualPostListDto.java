package com.dominest.dominestbackend.api.post.manual.dto;

import com.dominest.dominestbackend.api.common.AuditLog;
import com.dominest.dominestbackend.api.common.CategoryDto;
import com.dominest.dominestbackend.api.common.PageInfoDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.manual.ManualPost;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ManualPostListDto {
    @Getter
    public static class Res {
        CategoryDto category;
        PageInfoDto page;

        List<ManualPostDto> posts;

        public static Res from(Page<ManualPost> postPage, Category category){
            CategoryDto categoryDto = CategoryDto.from(category);
            PageInfoDto pageInfoDto = PageInfoDto.from(postPage);

            List<ManualPostDto> posts
                    = ManualPostDto.from(postPage);

            return new Res(pageInfoDto, posts, categoryDto);
        }

        Res(PageInfoDto page, List<ManualPostDto> posts, CategoryDto category) {
            this.page = page;
            this.posts = posts;
            this.category = category;
        }

        @Builder
        @Getter
        static class ManualPostDto {
            long id;
            String title;
            String writerName;
            boolean isModified;
            LocalDateTime createTime;
            AuditLog auditLog;

            static ManualPostDto from(ManualPost post){
                return ManualPostListDto.Res.ManualPostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .writerName(post.getWriter().getName())
                        .isModified(post.isModified())
                        .createTime(post.getCreateTime())
                        .auditLog(AuditLog.from(post))
                        .build();
            }

            static List<ManualPostDto> from(Page<ManualPost> posts){
                return posts.stream()
                        .map(ManualPostDto::from)
                        .collect(Collectors.toList());
            }
        }
    }
}
