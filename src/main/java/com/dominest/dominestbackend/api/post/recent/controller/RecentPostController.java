package com.dominest.dominestbackend.api.post.recent.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.post.recent.dto.RecentPostListDto;
import com.dominest.dominestbackend.domain.post.common.RecentPost;
import com.dominest.dominestbackend.domain.post.common.RecentPostService;
import com.dominest.dominestbackend.global.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RecentPostController {
    private final RecentPostService recentPostService;

    @GetMapping("/recent-posts")
    public RspTemplate<RecentPostListDto.Res> handleGetRecentPosts(@RequestParam(defaultValue = "1") int page) {
        final int IMAGE_TYPE_PAGE_SIZE = 20;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageableUtil.of(page, IMAGE_TYPE_PAGE_SIZE, sort);

        Page<RecentPost> recentPost = recentPostService.getRecentPosts(pageable);

        RecentPostListDto.Res resDto = RecentPostListDto.Res.from(recentPost);
        return new RspTemplate<>(HttpStatus.OK, "최근등록게시물 카테고리 링크 - 내림차순 조회", resDto);
    }
}
















