package com.dominest.dominestbackend.api.resident.controller;


import com.dominest.dominestbackend.api.common.ResTemplate;
import com.dominest.dominestbackend.api.resident.dto.ResidentPdfListDto;
import com.dominest.dominestbackend.api.resident.dto.ResidentListDto;
import com.dominest.dominestbackend.api.resident.dto.SaveResidentDto;
import com.dominest.dominestbackend.domain.resident.Resident;
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
import java.util.List;

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

    // 특정 입사생의 PDF 조회
    @GetMapping("/residents/{id}/pdf")
    public ResTemplate<?> handleGetPdf(@PathVariable Long id,  HttpServletResponse response){

        // filename 가져오기.
        Resident resident = residentService.findById(id);
        String filename = resident.getPdfFileName();

        // PDF 파일 읽기
        byte[] bytes = fileService.getByteArr(FileService.FilePrefix.RESIDENT_PDF, filename);

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);

        try(ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_SENT);
        }
        return new ResTemplate<>(HttpStatus.OK, "pdf 조회 성공");
    }

    // PDF 단건 업로드
    @PostMapping("/residents/{id}/pdf")
    public ResponseEntity<ResTemplate<String>> handlePdfUpload(@RequestParam(required = true) MultipartFile pdf, @PathVariable Long id){
        residentService.uploadPdf(id, FileService.FilePrefix.RESIDENT_PDF, pdf);
        ResTemplate<String> resTemplate = new ResTemplate<>(HttpStatus.CREATED, "pdf 업로드 완료");
        return ResponseEntity.created(URI.create("/residents/"+id+"/pdf")).body(resTemplate);
    }

    // PDF 전체 업로드
    @PostMapping("/residents/pdf")
    public ResponseEntity<ResTemplate<String>> handlePdfUpload(@RequestParam(required = true) List<MultipartFile> pdfs
                                                                                                                    , @RequestParam(required = true) ResidenceSemester residenceSemester){
        int uploadCount = residentService.uploadPdfs(FileService.FilePrefix.RESIDENT_PDF, pdfs, residenceSemester);

        ResTemplate<String> resTemplate = new ResTemplate<>(HttpStatus.CREATED, "pdf 업로드 완료. 저장된 파일 수: " + uploadCount + "개");
        return ResponseEntity.created(URI.create("/residents/pdf")).body(resTemplate);
    }

    // 해당차수 입사생 전체 PDF 조회
    @GetMapping("/residents/pdf")
    public ResTemplate<ResidentPdfListDto.Res> handleGetAllPdfs(@RequestParam(required = true) ResidenceSemester residenceSemester){
        ResidentPdfListDto.Res res = residentService.getAllPdfs(residenceSemester);
        return new ResTemplate<>(HttpStatus.OK, "pdf url 조회 성공", res);
    }
}

















