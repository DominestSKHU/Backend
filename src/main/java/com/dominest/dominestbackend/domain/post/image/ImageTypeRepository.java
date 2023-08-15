package com.dominest.dominestbackend.domain.post.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageTypeRepository extends JpaRepository<ImageType, Long> {
    @Query("select i from ImageType i join fetch i.writer where i.id = :imageTypeId")
    ImageType findByIdFetchWriter(Long imageTypeId);
}