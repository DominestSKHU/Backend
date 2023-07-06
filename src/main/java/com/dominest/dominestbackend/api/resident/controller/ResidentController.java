package com.dominest.dominestbackend.api.resident.controller;


import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ResidentController {

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        // iterate all datas and print to console
        sheet.forEach(row -> {
            StringBuffer sb = new StringBuffer();
            row.forEach(cell -> {
                switch (cell.getCellType()) {
                    case STRING:
                        sb.append(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        sb.append(NumberToTextConverter.toText(cell.getNumericCellValue()));
                        break;
                    // Handle other cell types as needed
                }
                sb.append(" ");
            });
            System.out.println(sb.toString());
        });
        workbook.close();

        return "success";
//        if (!file.isEmpty()) {
//            try {
//                List<String[]> excelData = ExcelUtil.parseExcel(file);
//
//                for (String[] row : excelData) {
//                    Resident resident = new Resident();
//                    resident.setName(row[0]);
//                    resident.setGender(row[1]);
//                    resident.setStudentId(row[2]);
//                    // Set other fields as needed
//
//                    residentRepository.save(resident);
//                }
//
//                // Success message or redirect to a success page
//                return "success";
//            } catch (IOException e) {
//                // Handle the exception, return an error message, or redirect to an error page
//                return "error";
//            }
//        } else {
//            // File is empty, return an error message or redirect to an error page
//            return "error";
//        }
    }
}
