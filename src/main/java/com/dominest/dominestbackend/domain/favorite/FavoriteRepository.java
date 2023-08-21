package com.dominest.dominestbackend.domain.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT f FROM Favorite f" +
            " JOIN FETCH f.category c" +
            " JOIN FETCH f.user u" +
            " WHERE f.category.id =:categoryId AND u.email = :userEmail")
    Favorite findByCategoryIdAndUserEmail(@Param("categoryId") Long categoryId, @Param("userEmail") String userEmail);

    @Query("SELECT f FROM Favorite f" +
            " JOIN FETCH f.category c" +
            " JOIN f.user u" +
            " WHERE u.email = :email")
    // userEmail은 조회결과가 아니라 조건이므로 Fetch하지 않아도 될 듯?
    List<Favorite> findAllByUserEmailFetchCategory(@Param("email") String email);
}
