package com.dominest.dominestbackend.domain.post.sanitationcheck;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SanitationCheckPostRepository extends JpaRepository<SanitationCheckPost, Long> {

    @Query(value = "SELECT p FROM SanitationCheckPost p" +
            " WHERE p.category.id = :categoryId"
            , countQuery = "SELECT count(p) FROM SanitationCheckPost p WHERE p.category.id = :categoryId")
    Page<SanitationCheckPost> findAllByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query(value = "SELECT p FROM SanitationCheckPost p" +
            " JOIN FETCH p.category" +
            " WHERE p.id = :postId")
    SanitationCheckPost findByIdFetchCategory(@Param("postId") Long postId);
}