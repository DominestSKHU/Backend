package com.dominest.dominestbackend.domain.room;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public Room getByAssignedRoom(String assignedRoom) {
        return EntityUtil.mustNotNull(
                roomRepository.findByAssignedRoom(assignedRoom)
                , ErrorCode.ROOM_NOT_FOUND_BY_ROOM_CODE);
    }
}
