package com.dominest.dominestbackend.api.post.sanitationcheck.dto;

import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateCheckedRoomDto {
    @NoArgsConstructor
    @Getter
    public static class Req {
        private Boolean indoor;
        private Boolean leavedTrash;
        private Boolean toilet;
        private Boolean shower;
        private Boolean prohibitedItem;
        @Enumerated(EnumType.STRING)
        private CheckedRoom.PassState passState; // 미통과 1차통과 2차통과...
        private String etc;
    }
}
