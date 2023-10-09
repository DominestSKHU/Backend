package com.dominest.dominestbackend.global.util;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import com.dominest.dominestbackend.global.exception.exceptions.file.FileIOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ExcelUtil {

    public static final int RESIDENT_COLUMN_COUNT = 21;

    public static List<List<String>> parseExcel(MultipartFile file) {
        if (! isExcelFile(file)) {
            throw new AppServiceException(ErrorCode.INVALID_FILE_EXTENSION);
        }

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

            List<String> rowData = new ArrayList<>(RESIDENT_COLUMN_COUNT); // default capacity 10이므로 컬럼개수만큼 공간 확보
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
                        cellValue = "";
                        break;
                }
                rowData.add(cellValue);
            }
            data.add(rowData);
        }
        return data;
    }

    public static void checkResidentColumnCount(List<List<String>> sheet) {
        Integer sheetColumnCount = Optional.ofNullable(sheet.get(0))
                .map(List::size)
                .orElse(0);

        if (sheetColumnCount != RESIDENT_COLUMN_COUNT){
            throw new BusinessException("읽어들인 컬럼 개수가 " +
                    RESIDENT_COLUMN_COUNT + "개가 아닙니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private static boolean isExcelFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String ext = extractExt(originalFileName);
        return "xlsx".equals(ext) || "xls".equals(ext);
    }

    private static String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos +1);
    }
}
