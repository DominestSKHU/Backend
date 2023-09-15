package com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckedRoomRepository extends JpaRepository<CheckedRoom, Long> {
    // CheckedRoom의 Resident Id는 Null일 수 있다.
    @Query("SELECT cr FROM CheckedRoom cr " +
            "JOIN FETCH cr.room " +
            "LEFT JOIN FETCH cr.resident " +
            "WHERE cr.floor.id = :floorId")
    List<CheckedRoom> findAllByFloorIdFetchResidentAndRoom(@Param("floorId") Long floorId);

    @Query("SELECT cr FROM CheckedRoom cr" +
            " LEFT JOIN FETCH cr.resident " +
            " WHERE cr.id = :id")
    CheckedRoom findByIdFetchResident(@Param("id") Long id);

    // 방역점검 게시글에 연관된 모든 CheckedRoom을 가져온다. 3중 조인해야 하며, 미통과만 조회한다,
    @Query("SELECT cr FROM CheckedRoom cr" +
            " JOIN FETCH cr.floor f" +
            " JOIN FETCH f.sanitationCheckPost p" +
            " JOIN FETCH cr.room" +
            " LEFT JOIN FETCH cr.resident" +

            " WHERE p.id = :postId" +
            " AND cr.passed = :passState")
    List<CheckedRoom> findNotPassedAllByPostId(@Param("postId") Long postId, @Param("passState") CheckedRoom.PassState passState);

    //  Todo 학기 정보 고려해야
    @Query("SELECT cr FROM CheckedRoom cr" +
            " WHERE cr.room.id = :roomId")
    List<CheckedRoom> findAllByResidentId(@Param("roomId") Long roomId);
}