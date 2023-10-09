package com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom;

import com.dominest.dominestbackend.domain.resident.Resident;
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
            " JOIN cr.floor f" +
            " JOIN FETCH cr.room" +
            " LEFT JOIN FETCH cr.resident" +
            " WHERE f.sanitationCheckPost.id = :postId" +
            " AND cr.passed = :passState")
    List<CheckedRoom> findNotPassedAllByPostId(@Param("postId") Long postId, @Param("passState") CheckedRoom.PassState passState);

    // 미통과 방의 입사생 목록을 가져온다.
    @Query("SELECT resi FROM CheckedRoom cr" +
            " JOIN cr.floor f" + // floor를 거쳐야 sanitationCheckPost에 접근할 수 있다. floor 데이터가 필요하지는 않으므로 fetch하지 않는다.
            " JOIN cr.resident resi" +
            " JOIN FETCH resi.room ro" +

            " WHERE f.sanitationCheckPost.id = :postId" +
            " AND cr.passed = :passState")
    List<Resident> findNotPassedResidentAllByPostId(@Param("postId") Long postId, @Param("passState") CheckedRoom.PassState passState);

    @Query("SELECT cr FROM CheckedRoom cr" +
            " WHERE cr.resident.id = :residentId")
    List<CheckedRoom> findAllByResidentId(@Param("residentId") Long residentId);
}