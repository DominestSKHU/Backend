package com.dominest.dominestbackend.domain.room;

import com.dominest.dominestbackend.domain.common.DomainName;
import com.dominest.dominestbackend.global.exception.exceptions.domain.EntityNotFoundException;
import com.dominest.dominestbackend.global.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
                , String.format("방 코드 -> %s <- 로 방을 찾을 수 없습니다", assignedRoom), HttpStatus.NOT_FOUND);
    }

    public Room getById(long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(DomainName.ROOM, id, HttpStatus.NOT_FOUND));
    }
}
