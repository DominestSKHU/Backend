package com.dominest.dominestbackend.api.room.dto;

import com.dominest.dominestbackend.domain.room.Room;
import com.dominest.dominestbackend.domain.room.roomhistory.RoomHistory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class RoomHistoryListResponse {
    String roomCode;
    List<RoomHistoryDto> roomHistories;
    public static RoomHistoryListResponse from(Room room, List<RoomHistory> roomHistories) {
        String roomCode = room.getAssignedRoom().substring(1);
        List<RoomHistoryDto> roomHistoryDtos = RoomHistoryDto.from(roomHistories);
        return new RoomHistoryListResponse(roomCode, roomHistoryDtos);
    }

    @AllArgsConstructor
    @Getter
    static class RoomHistoryDto {
        String residentName;
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul", shape = JsonFormat.Shape.STRING)
        LocalDate admissionDate;
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul", shape = JsonFormat.Shape.STRING)
        LocalDate leavingDate;
        String phoneNumber;
        String studentId;

        public static List<RoomHistoryDto> from(List<RoomHistory> roomHistories) {
            return roomHistories.stream()
                    .map(roomHistory -> new RoomHistoryDto(
                                roomHistory.getResidentName()
                                , roomHistory.getAdmissionDate(), roomHistory.getLeavingDate()
                                , roomHistory.getPhoneNumber(), roomHistory.getStudentId())
                    )
                    .collect(Collectors.toList());
        }
    }
}
