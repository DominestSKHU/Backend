package com.dominest.dominestbackend.domain.room;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByAssignedRoom(String assignedRoom);
}