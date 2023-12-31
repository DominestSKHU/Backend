package com.dominest.dominestbackend.api.post.sanitationcheck.dto;

import com.dominest.dominestbackend.api.common.AuditLog;
import com.dominest.dominestbackend.api.common.CategoryDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.component.ResidentInfo;
import com.dominest.dominestbackend.domain.resident.Resident;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckedRoomListDto {
    @Getter
    public static class Res {
        CategoryDto category;
        List<CheckedRoomDto> checkedRooms;
        public static CheckedRoomListDto.Res from(List<CheckedRoom> checkedRooms, Category category){
            CategoryDto categoryDto = CategoryDto.from(category);

            List<CheckedRoomDto> checkedRoomDtos
                    = CheckedRoomDto.from(checkedRooms);

            return new CheckedRoomListDto.Res(checkedRoomDtos, categoryDto);
        }

        Res(List<CheckedRoomDto> checkedRooms, CategoryDto category) {
            this.checkedRooms = checkedRooms;
            this.category = category;
        }

        @Builder
        @Getter
        static class CheckedRoomDto {
            long id;
            boolean emptyRoom;
            String assignedRoom;
            ResidentDto resident;

            boolean indoor;
            boolean leavedTrash;
            boolean toilet;
            boolean shower;
            boolean prohibitedItem;

            CheckedRoom.PassState passState;
            String etc;

            AuditLog auditLog;

            static CheckedRoomDto from(CheckedRoom checkedRoom){
                ResidentInfo residentInfo = checkedRoom.getResidentInfo();
                ResidentDto residentDto = null;
                boolean emptyRoom = true;

                if (residentInfo != null) {
                     residentDto = ResidentDto.builder()
                            .name(residentInfo.getName())
                            .studentId(residentInfo.getStudentId())
                            .phoneNo(residentInfo.getPhoneNo())
                            .penalty(checkedRoom.getPassState().getPenalty())
                            .build();
                     emptyRoom = false;
                }

                return CheckedRoomDto.builder()
                        .id(checkedRoom.getId())
                        .emptyRoom(emptyRoom)
                        .assignedRoom(checkedRoom.getRoom().getAssignedRoom())
                        .resident(residentDto)
                        .indoor(checkedRoom.isIndoor())
                        .leavedTrash(checkedRoom.isLeavedTrash())
                        .toilet(checkedRoom.isToilet())
                        .shower(checkedRoom.isShower())
                        .prohibitedItem(checkedRoom.isProhibitedItem())
                        .passState(checkedRoom.getPassState())
                        .etc(checkedRoom.getEtc())
                        .auditLog(AuditLog.from(checkedRoom))
                        .build();
            }

            static List<CheckedRoomDto> from(List<CheckedRoom> rooms){
                return rooms.stream()
                        .map(CheckedRoomDto::from)
                        .collect(Collectors.toList());
            }

            @Getter
            @Builder
            static class ResidentDto {
                String name;
                String studentId;
                @JsonProperty("phon") // 이현우씨의 속성명 요구...
                String phoneNo;
                Integer penalty;
            }
        }
    }
}
