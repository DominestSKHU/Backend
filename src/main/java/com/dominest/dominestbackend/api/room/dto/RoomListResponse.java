package com.dominest.dominestbackend.api.room.dto;

import com.dominest.dominestbackend.domain.room.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RoomListResponse {
    int floorNo;
    int roomCnt;
    List<RoomDto> rooms;

    public RoomListResponse(int floorNo, int roomCnt, List<Room> rooms) {

        this.floorNo = floorNo;
        this.roomCnt = roomCnt;
        this.rooms = RoomDto.from(rooms);
    }

    @Getter
    @AllArgsConstructor
    static class RoomDto {
        String roomCode;
        long sectionAId;
        long sectionBId;

        static List<RoomDto> from(List<Room> rooms) {
            // ID가 홀수면 A, 짝수면 B
            List<Room> roomAList = rooms.stream()
                    .filter(room -> room.getId() % 2 == 1)
                    .collect(Collectors.toList());
            List<Room> roomBList = rooms.stream()
                    .filter(room -> room.getId() % 2 == 0)
                    .collect(Collectors.toList());
            // 기본적으로 둘의 사이즈가 같지만, 만일을 대비해 둘 중 작은 사이즈를 기준으로 한다.
            int size = Math.min(roomAList.size(), roomBList.size());
            ArrayList<RoomDto> roomDtos = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Room roomA = roomAList.get(i);
                Room roomB = roomBList.get(i);
                roomDtos.add(RoomDto.from(roomA, roomB));
            }
            return roomDtos;
        }

        static RoomDto from(Room roomA, Room roomB) {
            String assignedRoom = roomA.getAssignedRoom();
            String roomCode = "";
            if (assignedRoom != null && assignedRoom.length() >= 6) {
                roomCode = roomA.getAssignedRoom().substring(1, 5);
            }

            return new RoomDto(roomCode, roomA.getId(), roomB.getId());
        }
    }
}
