package com.dominest.dominestbackend.domain.post.sanitationcheck.floor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FloorService {
    private final FloorRepository floorRepository;
}
