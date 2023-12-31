package com.dominest.dominestbackend.api.post.sanitationcheck.dto;


import com.dominest.dominestbackend.api.common.AuditLog;
import com.dominest.dominestbackend.api.common.CategoryDto;
import com.dominest.dominestbackend.api.common.PageInfoDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.sanitationcheck.SanitationCheckPost;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckPostListDto {
    @Getter
    public static class Res {
        CategoryDto category;
        PageInfoDto page;

        List<CheckPostDto> posts;
        public static Res from(Page<SanitationCheckPost> postPage, Category category){
            CategoryDto categoryDto = CategoryDto.from(category);
            PageInfoDto pageInfoDto = PageInfoDto.from(postPage);

            List<CheckPostDto> posts
                    = CheckPostDto.from(postPage);

            return new Res(pageInfoDto, posts, categoryDto);
        }

        Res(PageInfoDto page, List<CheckPostDto> posts, CategoryDto category) {
            this.page = page;
            this.posts = posts;
            this.category = category;
        }

        @Builder
        @Getter
        static class CheckPostDto {
            long id;
            String title;
            AuditLog auditLog;

            static CheckPostDto from(SanitationCheckPost post){
                return CheckPostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .auditLog(AuditLog.from(post))
                        .build();
            }

            static List<CheckPostDto> from(Page<SanitationCheckPost> posts){
                return posts.stream()
                        .map(CheckPostDto::from)
                        .collect(Collectors.toList());
            }
        }
    }
}
