package com.dominest.dominestbackend.domain.room;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByAssignedRoom(String assignedRoom);

    List<Room> findByFloorNo(Integer roomNo);
}