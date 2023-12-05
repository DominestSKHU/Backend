package com.dominest.dominestbackend.domain.room.roomhistory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoomHistoryService {
    private final RoomHistoryRepository roomHistoryRepository;
}
