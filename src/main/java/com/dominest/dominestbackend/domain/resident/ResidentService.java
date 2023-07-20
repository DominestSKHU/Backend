package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.api.resident.dto.ResidentListDto;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import com.dominest.dominestbackend.global.exception.exceptions.domain.EntityNotFoundException;
import com.dominest.dominestbackend.global.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ResidentService {
    private final ResidentRepository residentRepository;

    public Resident findById(Long id) {
        return residentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.RESIDENT_NOT_FOUND));
    }

    @Transactional
    public void excelUpload(MultipartFile file, ResidenceSemester residenceSemester) {
        // 예상 컬럼 개수
        int columnCount = ExcelUtil.RESIDENT_COLUMN_COUNT;

        // 엑셀 파싱
        List<List<String>> sheet= ExcelUtil.parseExcel(file);

        Integer sheetColumnCount = Optional.ofNullable(sheet.get(0))
                .map(List::size)
                .orElse(0);

        if (sheetColumnCount != columnCount){
            throw new AppServiceException("읽어들인 컬럼 개수가 " +
                    columnCount + "개가 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        // 첫 3줄 제거 후 유효 데이터만 추출
        sheet.remove(0); sheet.remove(0);sheet.remove(0);

        for (List<String> row : sheet) {
            if ("BLANK".equals(row.get(columnCount - 1))) // 빈 row 발견 시 continue;
                continue;
            Resident resident = Resident.from(row, residenceSemester);
            residentRepository.save(resident);
        }
    }

    public ResidentListDto.Res getAllResidentByResidenceSemester(ResidenceSemester residenceSemester) {
        List<Resident> residents = residentRepository.findAllByResidenceSemester(residenceSemester);
        return ResidentListDto.Res.from(residents);
    }
    // Todo 테스트용 나중에 지우기
    public ResidentListDto.Res getAllResidentByResidenceSemester() {
        List<Resident> residents = residentRepository.findAll();
        return ResidentListDto.Res.from(residents);
    }

    // 테스트용 전체삭제 API
    @Transactional
    public void deleteAllResident() {
        residentRepository.deleteAllInBatch();
    }

    @Transactional
    public void saveResident(Resident resident) {
        try {
            residentRepository.save(resident);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("학생 저장 실패, 잘못된 입력값입니다. 오류 메시지: " +
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void updateResident(Long id, Resident resident) {
        Resident residentToUpdate = findById(id);
        residentToUpdate.updateValueFrom(resident);
    }

    @Transactional
    public void deleteById(Long id) {
        Resident resident = findById(id);
        residentRepository.delete(resident);
    }
}
















