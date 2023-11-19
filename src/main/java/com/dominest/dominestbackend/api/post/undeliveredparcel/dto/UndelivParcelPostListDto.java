package com.dominest.dominestbackend.api.post.undeliveredparcel.dto;

import com.dominest.dominestbackend.api.common.AuditLog;
import com.dominest.dominestbackend.api.common.CategoryDto;
import com.dominest.dominestbackend.api.common.PageInfoDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.UndeliveredParcelPost;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UndelivParcelPostListDto {
    @Getter
    public static class Res {
        CategoryDto category;
        PageInfoDto page;

        List<UndelivParcelPostDto> posts;

        public static Res from(Page<UndeliveredParcelPost> postPage, Category category){
            CategoryDto categoryDto = CategoryDto.from(category);
            PageInfoDto pageInfoDto = PageInfoDto.from(postPage);

            List<UndelivParcelPostDto> posts
                    = UndelivParcelPostDto.from(postPage);

            return new Res(pageInfoDto, posts, categoryDto);
        }

        Res(PageInfoDto page, List<UndelivParcelPostDto> posts, CategoryDto category) {
            this.page = page;
            this.posts = posts;
            this.category = category;
        }

        @Builder
        @Getter
        static class UndelivParcelPostDto {
            long id;
            String title;
            AuditLog auditLog;

            static UndelivParcelPostDto from(UndeliveredParcelPost post){
                return UndelivParcelPostListDto.Res.UndelivParcelPostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .auditLog(AuditLog.from(post))
                        .build();
            }

            static List<UndelivParcelPostDto> from(Page<UndeliveredParcelPost> posts){
                return posts
                        .map(UndelivParcelPostDto::from)
                        .toList();
            }
        }
    }
}
