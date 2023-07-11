package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.api.resident.dto.ResidentListDto;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;
import com.dominest.dominestbackend.global.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public void excelUpload(MultipartFile file) {
        // 예상 컬럼 개수
        int columnCount = ExcelUtil.RESIDENT_COLUMN_COUNT;

        // 엑셀 파싱
        List<List<String>> sheet;
        sheet = ExcelUtil.parseExcel(file);

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
            Resident resident = Resident.from(row);
            residentRepository.save(resident);
        }
    }

    public ResidentListDto.Res getAllResident() {
        List<Resident> residents = residentRepository.findAll();
        return ResidentListDto.Res.from(residents);
    }
}
