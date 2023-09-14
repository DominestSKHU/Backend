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
    CheckedRoom findByIdFetchResident(Long id);
}