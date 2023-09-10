package com.dominest.dominestbackend.domain.post.cardkey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardKeyRepository extends JpaRepository<CardKey, Long> {
    Page<CardKey> findAllByCategoryIdAndNameStartsWith(Long id, String name, Pageable pageable);

    Page<CardKey> findAllByCategoryId(Long id, Pageable pageable);
}