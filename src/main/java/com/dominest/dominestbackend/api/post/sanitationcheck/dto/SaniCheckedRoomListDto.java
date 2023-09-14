package com.dominest.dominestbackend.api.post.sanitationcheck.dto;

import com.dominest.dominestbackend.api.common.AuditLog;
import com.dominest.dominestbackend.api.common.CategoryDto;
import com.dominest.dominestbackend.domain.post.component.category.Category;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import com.dominest.dominestbackend.domain.resident.Resident;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SaniCheckedRoomListDto {
    @Getter
    public static class Res {
        CategoryDto category;
        List<SaniCheckedRoomDto> checkedRooms;
        public static SaniCheckedRoomListDto.Res from(List<CheckedRoom> checkedRooms, Category category){
            CategoryDto categoryDto = CategoryDto.from(category);

            List<SaniCheckedRoomDto> checkedRoomDtos
                    = SaniCheckedRoomDto.from(checkedRooms);

            return new SaniCheckedRoomListDto.Res(checkedRoomDtos, categoryDto);
        }

        Res(List<SaniCheckedRoomDto> checkedRooms, CategoryDto category) {
            this.checkedRooms = checkedRooms;
            this.category = category;
        }

        @Builder
        @Getter
        static class SaniCheckedRoomDto {
            long id;
            boolean emptyRoom;
            String assignedRoom;
            ResidentDto resident;

            Boolean indoor;
            Boolean leavedTrash;
            Boolean toilet;
            Boolean shower;
            Boolean prohibitedItem;

            CheckedRoom.PassState passed;
            String etc;

            AuditLog auditLog;

            static SaniCheckedRoomDto from(CheckedRoom checkedRoom){
                Resident resident = checkedRoom.getResident();
                ResidentDto residentDto = null;
                boolean emptyRoom = true;
                if (resident != null) {
                     residentDto = ResidentDto.builder()
                            .name(resident.getName())
                            .studentId(resident.getStudentId())
                            .penalty(resident.getPenalty())
                            .build();
                     emptyRoom = false;
                }

                return SaniCheckedRoomDto.builder()
                        .id(checkedRoom.getId())
                        .emptyRoom(emptyRoom)
                        .assignedRoom(checkedRoom.getRoom().getAssignedRoom())
                        .resident(residentDto)
                        .indoor(checkedRoom.getIndoor())
                        .leavedTrash(checkedRoom.getLeavedTrash())
                        .toilet(checkedRoom.getToilet())
                        .shower(checkedRoom.getShower())
                        .prohibitedItem(checkedRoom.getProhibitedItem())
                        .passed(checkedRoom.getPassed())
                        .etc(checkedRoom.getEtc())
                        .auditLog(AuditLog.from(checkedRoom))
                        .build();
            }

            static List<SaniCheckedRoomDto> from(List<CheckedRoom> rooms){
                return rooms.stream()
                        .map(SaniCheckedRoomDto::from)
                        .collect(Collectors.toList());
            }

            @Getter
            @Builder
            static class ResidentDto {
                String name;
                String studentId;
                Integer penalty;
            }
        }
    }
}
