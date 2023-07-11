package com.dominest.dominestbackend.api.resident.controller;


import com.dominest.dominestbackend.api.common.SingleRspsTemplate;
import com.dominest.dominestbackend.domain.resident.ResidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class ResidentController {

    private final ResidentService residentService;

    @PostMapping("/upload")
    @Transactional
    public ResponseEntity<SingleRspsTemplate<String>> handleFileUpload(@RequestParam("file") MultipartFile file){

        residentService.excelUpload(file);
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

















