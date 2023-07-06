package com.dominest.dominestbackend.global.util;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {
    public static List<String[]> parseExcel(MultipartFile file) throws IOException {
        List<String[]> data = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            List<String> rowData = new ArrayList<>();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = "";
                switch (cell.getCellType()) {
                    case STRING:
                        cellValue = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        cellValue = String.valueOf(cell.getNumericCellValue());
                        break;
                    // Handle other cell types as needed
                }
                rowData.add(cellValue);
            }

            data.add(rowData.toArray(new String[0]));
        }
        workbook.close();

        return data;
    }
}
