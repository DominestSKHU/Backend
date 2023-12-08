package com.dominest.dominestbackend.api.room.controller;

import com.dominest.dominestbackend.api.common.RspTemplate;
import com.dominest.dominestbackend.api.room.dto.RoomHistoryListResponse;
import com.dominest.dominestbackend.api.room.dto.RoomListResponse;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.domain.room.Room;
import com.dominest.dominestbackend.domain.room.RoomRepository;
import com.dominest.dominestbackend.domain.room.RoomService;
import com.dominest.dominestbackend.domain.room.roomhistory.RoomHistory;
import com.dominest.dominestbackend.domain.room.roomhistory.RoomHistoryService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping("/rooms")
@RestController
public class RoomController {

    private final RoomRepository roomRepository;
    private final RoomHistoryService roomHistoryService;
    private final RoomService roomService;

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

    // 지정한 방 ID의 거주기록 조회
    @GetMapping("/{id}/history")
    public RspTemplate<RoomHistoryListResponse> getRoomHistory(
            @PathVariable("id") long id
    ) {
        // Room ID로 찾고, Response 로 내보낸다.
        Room room = roomService.getById(id);
        List<RoomHistory> roomHistories = roomRepository.findByRoomId(room.getId());

        RoomHistoryListResponse rspDto = RoomHistoryListResponse.from(room, roomHistories);
        return new RspTemplate<>(HttpStatus.OK
                , id + "번 방의 거주기록 조회"
                , rspDto);
    }

    // 현재 사생 데이터를 방 거주기록에 반영.
    // 거주기록 관련 기능이 사생데이터 업로드 기능 이후에 만들어졌기 때문에 배포 후 최초 한 번은 실행해야 한다.
    @GetMapping("/init-history")
    public String initRoomHistory(
            @RequestParam ResidenceSemester residenceSemester
            ) {
        roomHistoryService.initRoomHistory(residenceSemester);
        return "방 거주기록 초기화 완료";
    }

}














