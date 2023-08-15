package com.dominest.dominestbackend.domain.post.component.category.repository;

import com.dominest.dominestbackend.domain.post.component.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}