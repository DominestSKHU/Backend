package com.dominest.dominestbackend.domain.room;

import com.dominest.dominestbackend.domain.room.roomhistory.RoomHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByAssignedRoom(String assignedRoom);

    List<Room> findByFloorNo(Integer roomNo);

    @Query("SELECT rh FROM RoomHistory rh" +
            " WHERE rh.room.id = :roomId" +
            " ORDER BY rh.id DESC")
    List<RoomHistory> findByRoomId(@Param("roomId") long roomId);
}