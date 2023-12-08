package com.dominest.dominestbackend.domain.room.roomhistory;

import com.dominest.dominestbackend.domain.resident.Resident;
import com.dominest.dominestbackend.domain.resident.ResidentRepository;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoomHistoryService {
    private final RoomHistoryRepository roomHistoryRepository;
    private final ResidentRepository residentRepository;

    @Transactional
    public void saveFrom(Resident resident) {
        RoomHistory roomHistory = RoomHistory.builder()
                .residentName(resident.getName())
                .admissionDate(resident.getAdmissionDate())
                .leavingDate(resident.getLeavingDate())
                .phoneNumber(resident.getPhoneNumber())
                .studentId(resident.getStudentId())
                .room(resident.getRoom())
                .build();

        roomHistoryRepository.save(roomHistory);
    }

    @Transactional
    public void initRoomHistory(ResidenceSemester residenceSemester) {
        residentRepository.findAllByResidenceSemesterFetchRoom(residenceSemester)
                .forEach(this::saveFrom);
    }
}
