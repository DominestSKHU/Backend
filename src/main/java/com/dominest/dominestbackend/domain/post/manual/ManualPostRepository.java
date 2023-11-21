package com.dominest.dominestbackend.domain.post.manual;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManualPostRepository extends JpaRepository<ManualPost, Long> {

    @Query(value = "SELECT m FROM ManualPost m JOIN FETCH m.writer u" +
            " WHERE m.category.id = :categoryId "
            , countQuery = "SELECT count(*) FROM ManualPost m WHERE m.category.id = :categoryId")
    Page<ManualPost> findAllByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

   // @EntityGraph(attributePaths = {"writer", "attachmentUrls", "imageUrls", "videoUrls"})
   // @Query(value = "SELECT m FROM ManualPost m")
   // ManualPost findManualPostIncludeAllColumn(long manualId);

     @Query(value = "SELECT m, m.writer FROM ManualPost m")
     ManualPost findManualPostIncludeAllColumn(long manualId);
    void deleteByCategoryId(Long categoryId);
}
