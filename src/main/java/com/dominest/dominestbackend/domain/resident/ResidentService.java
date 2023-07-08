package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.global.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ResidentService {
    private final ResidentRepository residentRepository;

    @Transactional
    public void excelUpload(MultipartFile file) {
        List<List<String>> sheet;
        sheet = ExcelUtil.parseExcel(file);

        // 첫 3줄 제거 후 유효 데이터만 추출
        sheet.remove(0);
        sheet.remove(0);
        sheet.remove(0);

        for (List<String> row : sheet) {
            if ("BLANK".equals(row.get(20))) // 빈 row 발견 시 continue;
                continue;
            Resident resident = Resident.from(row);
            residentRepository.save(resident);
            System.out.println(resident.getId());
        }
    }
}
