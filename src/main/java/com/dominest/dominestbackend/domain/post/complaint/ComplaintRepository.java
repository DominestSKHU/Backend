package com.dominest.dominestbackend.domain.post.complaint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query(value = "SELECT c FROM Complaint c" +
            " WHERE c.category.id = :categoryId"
            , countQuery = "SELECT count(c) FROM Complaint c WHERE c.category.id = :categoryId")
    Page<Complaint> findAllByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}