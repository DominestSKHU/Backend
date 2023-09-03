package com.dominest.dominestbackend.api.post.undeliveredparcel.dto;

import com.dominest.dominestbackend.api.common.CategoryDto;
import com.dominest.dominestbackend.api.common.PageInfoDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.undeliveredparcel.UndeliveredParcelPost;
import com.dominest.dominestbackend.global.util.PrincipalUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UndelivParcelPostListDto {
    @Getter
    public static class Res {
        CategoryDto category;
        PageInfoDto page;

        List<UndelivParcelPostDto> posts;

        public static UndelivParcelPostListDto.Res from(Page<UndeliveredParcelPost> postPage, Category category){
            CategoryDto categoryDto = CategoryDto.from(category);
            PageInfoDto pageInfoDto = PageInfoDto.from(postPage);

            List<UndelivParcelPostListDto.Res.UndelivParcelPostDto> posts
                    = UndelivParcelPostListDto.Res.UndelivParcelPostDto.from(postPage);

            return new UndelivParcelPostListDto.Res(pageInfoDto, posts, categoryDto);
        }

        Res(PageInfoDto page, List<UndelivParcelPostListDto.Res.UndelivParcelPostDto> posts, CategoryDto category) {
            this.page = page;
            this.posts = posts;
            this.category = category;
        }

        @Builder
        @Getter
        static class UndelivParcelPostDto {
             long id;
            String title;
            String lastModifiedBy;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
            LocalDateTime lastModifiedTime;

            static UndelivParcelPostListDto.Res.UndelivParcelPostDto from(UndeliveredParcelPost post){
                return UndelivParcelPostListDto.Res.UndelivParcelPostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .lastModifiedBy(PrincipalUtil.strToName(post.getLastModifiedBy()))
                        .lastModifiedTime(post.getLastModifiedTime())
                        .build();
            }

            static List<UndelivParcelPostListDto.Res.UndelivParcelPostDto> from(Page<UndeliveredParcelPost> posts){
                return posts.stream()
                        .map(UndelivParcelPostListDto.Res.UndelivParcelPostDto::from)
                        .collect(Collectors.toList());
            }
        }
    }
}
