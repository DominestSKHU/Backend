package com.dominest.dominestbackend.api.resident.controller;


import com.dominest.dominestbackend.api.common.RspsTemplate;
import com.dominest.dominestbackend.api.resident.dto.ResidentListDto;
import com.dominest.dominestbackend.api.resident.dto.SaveResidentDto;
import com.dominest.dominestbackend.domain.resident.ResidentService;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ResidentController {

    private final ResidentService residentService;

    // 엑셀로 업로드
    @PostMapping("/residents/upload-excel")
    @Transactional
    public ResponseEntity<RspsTemplate<?>> handleFileUpload(@RequestParam(required = true) MultipartFile file
                                                                                                            , @RequestParam(required = true) ResidenceSemester residenceSemester){
        residentService.excelUpload(file, residenceSemester);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 전체조회
    @GetMapping("/residents")
    public RspsTemplate<ResidentListDto.Res> handleGetAllResident(@RequestParam(required = false) ResidenceSemester residenceSemester){
        ResidentListDto.Res residents = residentService.getAllResidentByResidenceSemester(residenceSemester);

        return new RspsTemplate<>(HttpStatus.OK, residents);
    }

    // (테스트용) 입사생 데이터 전체삭제
    @DeleteMapping("/residents")
    public ResponseEntity<RspsTemplate<?>> handleDeleteAllResident(){
        residentService.deleteAllResident();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 입사생 단건 등록. 단순 DTO 변환 후 저장만 하면 될듯
    @PostMapping("/residents")
    public ResponseEntity<RspsTemplate<?>> handleSaveResident(@RequestBody @Valid SaveResidentDto.Req reqDto){
        residentService.saveResident(reqDto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 입사생 수정
    @PatchMapping("/residents/{id}")
    public ResponseEntity<RspsTemplate<?>> handleUpdateResident(@PathVariable Long id, @RequestBody @Valid SaveResidentDto.Req reqDto){
        residentService.updateResident(id, reqDto.toEntity());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 입사생 삭제
    @DeleteMapping("/residents/{id}")
    public ResponseEntity<RspsTemplate<?>> handleDeleteResident(@PathVariable Long id){
        residentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }









//    StringBuilder sb = new StringBuilder();
//        for (List<String> strings : sheet) {
//        for (String string : strings) {
//            sb.append(string).append(" ");
//        }
//        sb.append("\n");
//    }
//        System.out.println(sb);
//        System.out.println("row 수 => "+sheet.size());
//        System.out.println("column 수 => "+sheet.get(0).size());
}

















