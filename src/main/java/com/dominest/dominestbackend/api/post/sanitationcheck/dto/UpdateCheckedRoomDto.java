package com.dominest.dominestbackend.api.post.sanitationcheck.dto;

import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateCheckedRoomDto {
    @NoArgsConstructor
    @Getter
    public static class Req {
        Boolean indoor;
        Boolean leavedTrash;
        Boolean toilet;
        Boolean shower;
        Boolean prohibitedItem;
        CheckedRoom.PassState passState; // 미통과 1차통과 2차통과...
        String etc;
    }
}
