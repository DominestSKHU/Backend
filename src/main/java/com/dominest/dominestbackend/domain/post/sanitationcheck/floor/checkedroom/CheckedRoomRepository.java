package com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom;

import com.dominest.dominestbackend.domain.resident.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckedRoomRepository extends JpaRepository<CheckedRoom, Long> {
    // CheckedRoom의 Resident Id는 Null일 수 있다. 모든 CheckedRoom을 조회해야 하므로 Left Join Resident로 조회한다.
    @Query("SELECT cr FROM CheckedRoom cr " +
            "JOIN FETCH cr.room " +
            "WHERE cr.floor.id = :floorId")
    List<CheckedRoom> findAllByFloorIdFetchResidentAndRoom(@Param("floorId") Long floorId);

    //모든 CheckedRoom을 조회해야 하므로 Left Join Resident로 조회한다.
    @Query("SELECT cr FROM CheckedRoom cr" +
            " WHERE cr.id = :id")
    CheckedRoom findByIdFetchResident(@Param("id") Long id);

    // 방역점검 게시글에 연관된 모든 CheckedRoom을 가져온다. 3중 조인해야 하며, 미통과만 조회한다,
    //모든 CheckedRoom을 조회해야 하므로 Left Join Resident로 조회한다.
    @Query("SELECT cr FROM CheckedRoom cr" +
            " JOIN cr.floor f" +
            " JOIN FETCH cr.room" +
            " WHERE f.sanitationCheckPost.id = :postId" +
            " AND cr.passState = :passState")
    List<CheckedRoom> findNotPassedAllByPostId(@Param("postId") Long postId, @Param("passState") CheckedRoom.PassState passState);

    // SaniCheckPost Id에 속한 CheckedRoom 전체를 조회한다.
    //모든 CheckedRoom을 조회해야 하므로 Left Join Resident로 조회한다.
    @Query("SELECT cr FROM CheckedRoom cr" +
            " JOIN cr.floor f" + // floor를 거쳐야 sanitationCheckPost에 접근할 수 있다. floor 데이터가 필요하지는 않으므로 fetch하지 않는다.
            " JOIN FETCH cr.room ro" +

            " WHERE f.sanitationCheckPost.id = :postId")
    List<CheckedRoom> findAllByPostId(@Param("postId")Long postId);

    // SaniCheckPost Id에 속한 CheckedRoom 전체를 조회한다.
    // 빈 방은 조회하지 않을 것이므로 Inner Join Resident로 조회한다.
    // Resident와 Room을 Fetch Join하며, passState를 Not In으로 조회한다.
    @Query("SELECT cr FROM CheckedRoom cr" +
            " JOIN cr.floor f" + // floor를 거쳐야 sanitationCheckPost에 접근할 수 있다. floor 데이터가 필요하지는 않으므로 fetch하지 않는다.
            " JOIN FETCH cr.room ro" +

            " WHERE f.sanitationCheckPost.id = :postId" +
            " AND cr.passState NOT IN :passStates")
    List<CheckedRoom> findAllByPostIdAndNotInPassState(@Param("postId")Long postId, @Param("passStates")List<CheckedRoom.PassState> passStates);

    // SaniCheckPost Id에 속한 CheckedRoom 전체를 조회한다.
    // 빈 방은 조회하지 않을 것이므로 Inner Join Resident로 조회한다.
    // Resident와 Room을 Fetch Join한다.
    @Query("SELECT cr FROM CheckedRoom cr" +
            " JOIN cr.floor f" + // floor를 거쳐야 sanitationCheckPost에 접근할 수 있다. floor 데이터가 필요하지는 않으므로 fetch하지 않는다.
            " JOIN FETCH cr.room ro" +

            " WHERE f.sanitationCheckPost.id = :postId" +
            " AND cr.passState = :passState")
    List<CheckedRoom> findAllByPostIdAndPassState(@Param("postId")Long postId, @Param("passState")CheckedRoom.PassState passState);
}