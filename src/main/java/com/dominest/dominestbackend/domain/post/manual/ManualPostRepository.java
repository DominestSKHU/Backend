package com.dominest.dominestbackend.domain.post.manual;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Entity;

public interface ManualPostRepository extends JpaRepository<ManualPost, Long> {

    @Query(value = "SELECT m FROM ManualPost m JOIN FETCH m.writer u" +
            " WHERE m.category.id = :categoryId "
            , countQuery = "SELECT count(*) FROM ManualPost m WHERE m.category.id = :categoryId")
    Page<ManualPost> findAllByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

   // @EntityGraph(attributePaths = {"writer", "attachmentUrls", "imageUrls", "videoUrls"})
   // @Query(value = "SELECT m FROM ManualPost m")
   // ManualPost findManualPostIncludeAllColumn(long manualId);

    //@EntityGraph(attributePaths = {"writer", "imageUrls", "attachmentUrls", "videoUrls"})
    @Query(value = "SELECT m FROM ManualPost m left join fetch m.imageUrls left join fetch m.writer " +
            "left join fetch m.attachmentUrls left join fetch m.videoUrls")
    ManualPost findManualPostIncludeAllColumn(long manualId);
    void deleteByCategoryId(Long categoryId);
}
