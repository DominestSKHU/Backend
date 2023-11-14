package com.dominest.dominestbackend.domain.post.manual;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;

public interface ManualPostRepository extends JpaRepository<ManualPost, Long> {

    void deleteByCategoryId(Long categoryId);
}
