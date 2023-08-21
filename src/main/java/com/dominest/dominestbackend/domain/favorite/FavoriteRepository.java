package com.dominest.dominestbackend.domain.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT f FROM Favorite f" +
            " JOIN FETCH f.category c" +
            " JOIN FETCH f.user u" +
            " WHERE f.category.id =:categoryId AND u.email = :userEmail")
    Favorite findByCategoryIdAndUserEmail(@Param("categoryId") Long categoryId, @Param("userEmail") String userEmail);
}
