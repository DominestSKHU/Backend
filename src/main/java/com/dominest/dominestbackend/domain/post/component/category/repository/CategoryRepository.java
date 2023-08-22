package com.dominest.dominestbackend.domain.post.component.category.repository;

import com.dominest.dominestbackend.domain.post.component.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT f.category.id FROM Favorite f" +
            " JOIN  f.category c" +
            " JOIN f.user u" +
            " WHERE u.email = :email AND f.onOff = true")
        // userEmail은 조회결과가 아니라 조건이므로 Fetch하지 않아도 될 듯?
    List<Long> findIdAllByUserEmailFetchCategory(@Param("email") String email);
}