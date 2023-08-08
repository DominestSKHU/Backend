package com.dominest.dominestbackend.domain.category.repository;

import com.dominest.dominestbackend.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}