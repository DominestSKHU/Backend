package com.dominest.dominestbackend.domain.room;

import com.dominest.dominestbackend.global.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public Room getByAssignedRoom(String assignedRoom) {
        return EntityUtil.mustNotNull(
                roomRepository.findByAssignedRoom(assignedRoom)
                , String.format("방 코드 -> %s <- 로 방을 찾을 수 없습니다", assignedRoom), HttpStatus.NOT_FOUND);
    }

    public List<Room> getByFloorNo(Integer roomNo) {
        return roomRepository.findByFloorNo(roomNo);
    }
}
