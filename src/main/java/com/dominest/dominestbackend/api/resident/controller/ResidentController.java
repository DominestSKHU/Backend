package com.dominest.dominestbackend.api.resident.controller;


import com.dominest.dominestbackend.api.common.RspsTemplate;
import com.dominest.dominestbackend.api.resident.dto.ResidentListDto;
import com.dominest.dominestbackend.api.resident.dto.SaveResidentDto;
import com.dominest.dominestbackend.domain.resident.ResidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class ResidentController {

    private final ResidentService residentService;

    // 엑셀로 업로드
    @PostMapping("/residents/upload-excel")
    @Transactional
    public ResponseEntity<RspsTemplate<?>> handleFileUpload(@RequestParam("file") MultipartFile file){

        residentService.excelUpload(file);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 전체조회
    @GetMapping("/residents")
    public RspsTemplate<ResidentListDto.Res> getAllResident(){
        ResidentListDto.Res residents = residentService.getAllResident();
        return new RspsTemplate<>(HttpStatus.OK, residents);
    }

    // (테스트용) 입사생 데이터 전체삭제
    @DeleteMapping("/residents")
    public ResponseEntity<RspsTemplate<?>> deleteAllResident(){
        residentService.deleteAllResident();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 학생 단건 등록. 단순 DTO 변환 후 저장만 하면 될듯
    @PostMapping("/residents")
    public ResponseEntity<RspsTemplate<String>> saveResident(@RequestBody SaveResidentDto.Req reqDto){
        residentService.saveResident(reqDto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
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

















