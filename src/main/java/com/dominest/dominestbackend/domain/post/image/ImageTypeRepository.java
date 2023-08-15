package com.dominest.dominestbackend.domain.post.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageTypeRepository extends JpaRepository<ImageType, Long> {
    @Query("select i from ImageType i join fetch i.writer where i.id = :imageTypeId")
    ImageType findByIdFetchWriter(Long imageTypeId);

    @Query(value = "SELECT i FROM ImageType i JOIN FETCH i.writer"
            , countQuery = "SELECT count(i) FROM ImageType i")
    Page<ImageType> findAllFetchWriter(Pageable pageable);
}