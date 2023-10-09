package com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom;

import com.dominest.dominestbackend.api.post.sanitationcheck.dto.UpdateCheckedRoomDto;
import com.dominest.dominestbackend.api.resident.dto.ResidentListDto;
import com.dominest.dominestbackend.domain.resident.Resident;
import com.dominest.dominestbackend.domain.resident.penalty.PenaltyHist;
import com.dominest.dominestbackend.domain.resident.penalty.PenaltyHistService;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import com.dominest.dominestbackend.global.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CheckedRoomService {
    private final CheckedRoomRepository checkedRoomRepository;
    private final PenaltyHistService penaltyHistService;

    @Transactional
    public List<CheckedRoom> create(List<CheckedRoom> checkedRooms) {
        try {
            return checkedRoomRepository.saveAll(checkedRooms);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("CheckedRoom 저장 실패, 중복 혹은 값의 누락을 확인해주세요"
                    , HttpStatus.BAD_REQUEST);
        }
    }

    public CheckedRoom getById(Long id) {
        return EntityUtil.mustNotNull(checkedRoomRepository.findById(id), ErrorCode.CHECKED_ROOM_NOT_FOUND);
    }

    public List<CheckedRoom> getAllByFloorId(Long floorId) {
        return checkedRoomRepository.findAllByFloorIdFetchResidentAndRoom(floorId);
    }

    public CheckedRoom getByIdFetchResident(Long id) {
        return EntityUtil.mustNotNull(checkedRoomRepository.findByIdFetchResident(id), ErrorCode.CHECKED_ROOM_NOT_FOUND);
    }

    @Transactional
    public void update(Long checkedRoomId, UpdateCheckedRoomDto.Req reqDto) { // api 호출 편의성을 위해 이 ReqDto는 값 검증하지 않았음.
        CheckedRoom checkedRoom = getByIdFetchResident(checkedRoomId);
        // Null이 아닌 값만 업데이트
        checkedRoom.updateValuesOnlyNotNull(
                reqDto.getIndoor()
                , reqDto.getLeavedTrash()
                , reqDto.getToilet()
                , reqDto.getShower()
                , reqDto.getProhibitedItem()
                , reqDto.getPassState()
                , reqDto.getEtc()
        );

        Resident resident = checkedRoom.getResident();
        CheckedRoom.PassState passState = reqDto.getPassState();

        // 벌점이 매겨진 기록이 있는지 확인한 후 벌점증가처리.
        // 입사생 정보가 없거나, PassState가 null이면 벌점증가를 신경쓸 필요가 없다.
        if (resident != null && passState != null) {
            PenaltyHist penaltyHist = penaltyHistService.findByResidentIdAndCheckedRoomId(resident.getId(), checkedRoomId);
            if (penaltyHist != null) { // 입사생도 존재하고, State도 변경되었고, 기록이 있다면 기록을 변경하고 벌점반영
                resident.changePenalty(penaltyHist.getPenalty(), passState.getPenalty());
                penaltyHist.setPenalty(passState.getPenalty());
            } else { // 기록이 없다면 벌점을 반영하고 기록 생성
                resident.increasePenalty(passState.getPenalty());
                PenaltyHist newPenaltyHist = PenaltyHist.builder()
                        .resident(resident)
                        .checkedRoom(checkedRoom)
                        .penalty(passState.getPenalty())
                        .build();
                penaltyHistService.create(newPenaltyHist);
            }
        }
    }

    @Transactional
    public void passAll(Long roomId) {
        CheckedRoom checkedRoom = getById(roomId);
        checkedRoom.passAll();
    }
}














