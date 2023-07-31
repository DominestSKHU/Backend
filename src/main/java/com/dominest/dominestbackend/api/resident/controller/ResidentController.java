package com.dominest.dominestbackend.api.resident.controller;


import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.api.resident.dto.ResidentListDto;
import com.dominest.dominestbackend.api.resident.dto.SaveResidentDto;
import com.dominest.dominestbackend.domain.resident.ResidentService;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.file.FileIOException;
import com.dominest.dominestbackend.global.util.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
@RestController
public class ResidentController {

    private final ResidentService residentService;
    private final FileService fileService;

    // 엑셀로 업로드
    @PostMapping("/residents/upload-excel")
    public ResponseEntity<ResTemplate<?>> handleFileUpload(@RequestParam(required = true) MultipartFile file
                                                                                                            , @RequestParam(required = true) ResidenceSemester residenceSemester){
        residentService.excelUpload(file, residenceSemester);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    // 특정 입사생의 PDF 조회
    @GetMapping("/residents/{id}/pdf")
    public ResponseEntity<ResTemplate<?>> handlePdf(@RequestParam(required = true) String filename,
                                                                                                 HttpServletResponse response){
        byte[] bytes = fileService.getByteArr("test.pdf");
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);

        try(ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_SENT);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // PDF 단건 업로드
    @PostMapping("/residents/{id}/pdf")
    public ResponseEntity<ResTemplate<String>> handlePdfUpload(@RequestParam MultipartFile pdf, @PathVariable Long id){
        String uploadedFileName = residentService.uploadPdf(id, FileService.FilePrefix.RESIDENT_PDF, pdf);
        ResTemplate<String> resTemplate = new ResTemplate<>(HttpStatus.CREATED, "pdf 업로드 완료", null);
        // Todo 조회API 만들고 location Header 다시 확인하기
        return ResponseEntity.created(URI.create("/residents/"+id+"/pdf/" + uploadedFileName)).body(resTemplate);
    }

    // 전체조회
    @GetMapping("/residents")
    public ResTemplate<ResidentListDto.Res> handleGetAllResident(@RequestParam(required = true) ResidenceSemester residenceSemester){
        ResidentListDto.Res residents = residentService.getAllResidentByResidenceSemester(residenceSemester);
        return new ResTemplate<>(HttpStatus.OK, "입사생 목록 조회 성공", residents);
    }

    // (테스트용) 입사생 데이터 전체삭제
    @DeleteMapping("/residents")
    public ResponseEntity<ResTemplate<?>> handleDeleteAllResident(){
        residentService.deleteAllResident();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 입사생 단건 등록. 단순 DTO 변환 후 저장만 하면 될듯
    @PostMapping("/residents")
    public ResponseEntity<ResTemplate<?>> handleSaveResident(@RequestBody @Valid SaveResidentDto.Req reqDto){
        residentService.saveResident(reqDto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 입사생 수정
    @PatchMapping("/residents/{id}")
    public ResponseEntity<ResTemplate<?>> handleUpdateResident(@PathVariable Long id, @RequestBody @Valid SaveResidentDto.Req reqDto){
        residentService.updateResident(id, reqDto.toEntity());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 입사생 삭제
    @DeleteMapping("/residents/{id}")
    public ResponseEntity<ResTemplate<?>> handleDeleteResident(@PathVariable Long id){
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

















