package com.dominest.dominestbackend.domain.post.sanitationcheck;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SanitationCheckPostRepository extends JpaRepository<SanitationCheckPost, Long> {
}