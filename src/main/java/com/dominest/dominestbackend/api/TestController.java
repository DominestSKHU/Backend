package com.dominest.dominestbackend.api;

import com.dominest.dominestbackend.domain.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final RoomRepository roomRepository;

    @GetMapping("/health")
    public String getRoom(){
        return "231004 1701";
    }

}
