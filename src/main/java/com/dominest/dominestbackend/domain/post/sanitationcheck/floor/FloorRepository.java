package com.dominest.dominestbackend.domain.post.sanitationcheck.floor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, Long> {

    @Query("SELECT f FROM Floor f " +
            "where f.sanitationCheckPost.id = :postId")
    List<Floor> findAllByPostId(@Param("postId") Long postId);
}