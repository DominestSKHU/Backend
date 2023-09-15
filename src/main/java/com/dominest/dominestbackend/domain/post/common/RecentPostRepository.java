package com.dominest.dominestbackend.domain.post.common;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentPostRepository extends JpaRepository<RecentPost, Long> {
}