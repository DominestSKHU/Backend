package com.dominest.dominestbackend.domain.resident.penalty;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PenaltyHistService {
    private final PenaltyHistRepository penaltyHistRepository;

    @Transactional
    public PenaltyHist create(PenaltyHist penaltyHist) {
        return penaltyHistRepository.save(penaltyHist);
    }

    public boolean existsByResidentAndCheckedRoom(Long residentId, Long checkedRoomId) {
        return penaltyHistRepository.existsByResidentIdAndCheckedRoomId(residentId, checkedRoomId);
    }

    public PenaltyHist findByResidentIdAndCheckedRoomId(Long residentId, Long checkedRoomId) {
        return penaltyHistRepository.findByResidentIdAndCheckedRoomId(residentId, checkedRoomId);
    }
}

