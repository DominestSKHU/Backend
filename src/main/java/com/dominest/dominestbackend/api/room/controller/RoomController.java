package com.dominest.dominestbackend.api.room.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.room.dto.RoomListResponse;
import com.dominest.dominestbackend.domain.room.Room;
import com.dominest.dominestbackend.domain.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping("/rooms")
@RestController
public class RoomController {

    private final RoomRepository roomRepository;

    // floor[2-10] 에 맞는 방들을 가져온다.
    @GetMapping
    public RspTemplate<RoomListResponse> getRooms(
            @RequestParam(value = "floor") @Range(min = 2, max = 10) int floor
    ) {
        // Floor로 찾고, Response 로 내보낸다.
        List<Room> rooms = roomRepository.findByFloorNo(floor);

        RoomListResponse roomListResponse = new RoomListResponse(floor, rooms.size(), rooms);
        return new RspTemplate<>(HttpStatus.OK
                , floor + "층의 방 정보 조회"
                ,roomListResponse);
    }
}














