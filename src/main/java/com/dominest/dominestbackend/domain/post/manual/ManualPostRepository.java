package com.dominest.dominestbackend.domain.post.manual;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManualPostRepository extends JpaRepository<ManualPost, Long> {

    void deleteByCategoryId(Long categoryId);
}
