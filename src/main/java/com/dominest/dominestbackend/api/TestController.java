package com.dominest.dominestbackend.api;

import com.dominest.dominestbackend.domain.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final RoomRepository roomRepository;
    @GetMapping("/test/exception")
    public String throwNPE(){
        throw new NullPointerException();
    }

    @GetMapping("/test/room")
    public String getRoom(){
        return roomRepository.findAll().get(0).toString();
    }

}
