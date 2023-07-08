package com.dominest.dominestbackend.global.util;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.file.FileIOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class ExcelUtil {

    public static List<List<String>> parseExcel(MultipartFile file) {
        final int COLUMN_COUNT = 21;
        List<List<String>> data = new ArrayList<>();
        Sheet sheet;

        try (
                InputStream inputStream = file.getInputStream();
                Workbook workbook = WorkbookFactory.create(inputStream)
        ) {
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            log.error("ExcelUtil.parseExcel() : Excel  to Sheet Error ", e);
            throw new FileIOException(ErrorCode.MULTIPART_FILE_CANNOT_BE_READ);
        }

        // sheet extend Iterable<Row>
        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();

            List<String> rowData = new ArrayList<>(COLUMN_COUNT); // default capacity 10이므로 컬럼개수만큼 공간 확보
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = "";
                switch (cell.getCellType()) {
                    case STRING:
                        cellValue = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                        break;
                    case BLANK:
                        cellValue = "BLANK";
                        break;
                }
                rowData.add(cellValue);
            }
            data.add(rowData);
        }
        return data;
    }
}
