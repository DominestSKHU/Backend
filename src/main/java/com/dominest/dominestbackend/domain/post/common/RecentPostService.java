package com.dominest.dominestbackend.domain.post.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecentPostService {
    private final RecentPostRepository recentPostRepository;

    @Transactional
    public void create(RecentPost recentPost) {
        recentPostRepository.save(recentPost);
    }

    public Page<RecentPost> getRecentPosts(Pageable pageable) {
        return recentPostRepository.findAll(pageable);
    }
}
