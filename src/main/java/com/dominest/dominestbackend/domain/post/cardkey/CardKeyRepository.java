package com.dominest.dominestbackend.domain.post.cardkey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardKeyRepository extends JpaRepository<CardKey, Long> {
    @Query(value = "SELECT c FROM CardKey c" +
            " WHERE c.category.id = :categoryId AND c.name LIKE CONCAT(:name, '%')")
    Page<CardKey> findAllByCategoryIdAndNameStartsWith(@Param("categoryId") Long categoryId, @Param("name") String name, Pageable pageable);

    @Query(value = "SELECT c FROM CardKey c" +
                " WHERE c.category.id = :categoryId")
    Page<CardKey> findAllByCategoryId(@Param("categoryId") Long id, Pageable pageable);

    void deleteByCategoryId(Long categoryId);
}