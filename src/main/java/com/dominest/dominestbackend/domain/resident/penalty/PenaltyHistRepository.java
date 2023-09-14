package com.dominest.dominestbackend.domain.resident.penalty;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyHistRepository extends JpaRepository<PenaltyHist, Long> {
    PenaltyHist findByResidentIdAndCheckedRoomId(Long residentId, Long checkedRoomId);
    boolean existsByResidentIdAndCheckedRoomId(Long residentId, Long checkedRoomId);
}