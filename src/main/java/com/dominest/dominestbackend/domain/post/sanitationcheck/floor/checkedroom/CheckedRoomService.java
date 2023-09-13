package com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CheckedRoomService {
    private final CheckedRoomRepository checkedRoomRepository;
}
