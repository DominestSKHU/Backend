package com.dominest.dominestbackend.global.util;

import com.dominest.dominestbackend.domain.post.complaint.Complaint;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.component.ResidentInfo;
import com.dominest.dominestbackend.domain.room.Room;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import com.dominest.dominestbackend.global.exception.exceptions.file.FileIOException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 엑셀 파일 관련 작업을 하는 유틸리티 클래스
 */
// filename과 같은 String 파라미터를 객체로 래핑할 수도 있겠으나, 그냥 파라미터에 null이 들어오지 않게끔 조심하도록 했음. https://www.youtube.com/watch?v=oHaGgLRZy3Y 요 내용이랑 겹치는 상황
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

            // sheet extend Iterable<Row>.
            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();

                List<String> rowData = new ArrayList<>(RESIDENT_COLUMN_COUNT); // default capacity 10이므로 컬럼개수만큼 공간 확보
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellValue;
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
                        default:
                            cellValue = cell.getStringCellValue();
                            break;
                    }
                    rowData.add(cellValue);
                }
                data.add(rowData);
            }
            return data;
        } catch (IOException e) {
            throw new FileIOException(ErrorCode.MULTIPART_FILE_CANNOT_BE_READ, e);
        }
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
        String filename = file.getOriginalFilename();
        return isExcelExt(filename);
    }

    private static boolean isExcelExt(String fileName) {
        String ext = extractExt(fileName);
        return "xlsx".equals(ext) || "xls".equals(ext);
    }

    private static String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos +1);
    }

    // 통과차수별 방 정보와 소속된 사생의 정보를 반환.
    public static void createAndRespondResidentInfoWithCheckedRoom(String filename, String sheetName, HttpServletResponse response, List<CheckedRoom> checkedRooms) {
        if (! isExcelExt(filename)) {
            throw new AppServiceException(ErrorCode.INVALID_FILE_EXTENSION);
        }

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

        // try-with-resources를 사용하여 워크북 생성
        try (Workbook workbook = new XSSFWorkbook()) {
            // 새로운 워크시트 생성
            Sheet sheet = workbook.createSheet(sheetName);
            // 헤더 행 작성
            Row headerRow = sheet.createRow(0);
            String[] headers = {"호실", "이름", "전화번호", "학번", "벌점", "통과차수"};

            for (int i=0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 보기 편하게끔, 전화번호, 학번 컬럼은 width를 따로 설정
            int columnWidth14 = 14 * 256; // 14문자 너비
            int columnWidth10 = 10 * 256; // 10문자 너비
            sheet.setColumnWidth(2, columnWidth14);
            sheet.setColumnWidth(3, columnWidth10);

            // 데이터 작성
            for (int rowNum = 1; rowNum <= checkedRooms.size(); rowNum++) {
                Row row = sheet.createRow(rowNum);

                CheckedRoom checkedRoom = checkedRooms.get(rowNum - 1);
                ResidentInfo residentInfo = checkedRoom.getResidentInfo();
                Room room = checkedRoom.getRoom();
                String assignedRoom = room != null ? room.getAssignedRoom() : "";

                row.createCell(0).setCellValue(assignedRoom);
                row.createCell(1).setCellValue(residentInfo == null ? "" : residentInfo.getName());
                row.createCell(2).setCellValue(residentInfo == null ? "" : residentInfo.getPhoneNo());
                row.createCell(3).setCellValue(residentInfo == null ? "" : residentInfo.getStudentId());
                row.createCell(4).setCellValue(checkedRoom.getPassState().getPenalty());
                row.createCell(5).setCellValue(checkedRoom.getPassState().getValue());
            }

            // 파일 내보내기
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_SENT, e);
        }
    }

    // 점검표 화면의 내용 전체를 다운로드
    public static void createAndRespondAllDataWithCheckedRoom(String filename, String sheetName, HttpServletResponse response, List<CheckedRoom> checkedRooms) {
        if (! isExcelExt(filename)) {
            throw new AppServiceException(ErrorCode.INVALID_FILE_EXTENSION);
        }

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

        // try-with-resources를 사용하여 워크북 생성
        try (Workbook workbook = new XSSFWorkbook()) {
            // 새로운 워크시트 생성
            Sheet sheet = workbook.createSheet(sheetName);

            /* 첫 행에 부가정보 표시 START*/
            Row firstRow = sheet.createRow(0);

            // 셀 생성 및 데이터 입력
            Cell mergedDataCell = firstRow.createCell(0);
            mergedDataCell.setCellValue("입사생 정보가 비어있을 경우 빈 방입니다.");

            // 1행의 1열부터 5열까지 셀을 병합
            sheet.addMergedRegion(new CellRangeAddress(
                    0, // first row (0-based)
                    0, // last row
                    0, // first column (0-based)
                    4  // last column
            ));
            /* 첫 행에 부가정보 표시 END*/

            // 헤더 행 작성
            Row headerRow = sheet.createRow(1);
            String[] headers = {"호실", "이름", "전화번호", "학번", "벌점", "통과차수", "실내", "쓰레기방치", "화장실", "샤워실", "보관금지", "기타"};

            for (int i=0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 보기 편하게끔, 전화번호, 학번 컬럼은 width를 따로 설정
            int columnWidth14 = 14 * 256; // 14문자 너비
            int columnWidth10 = 10 * 256; // 10문자 너비
            sheet.setColumnWidth(2, columnWidth14);
            sheet.setColumnWidth(3, columnWidth10);

            // 데이터 작성
            int dataStartRow = 2;
            for (int rowNum = dataStartRow; rowNum <= checkedRooms.size() + dataStartRow - 1; rowNum++) {
                Row row = sheet.createRow(rowNum);

                CheckedRoom checkedRoom = checkedRooms.get(rowNum - dataStartRow);
                ResidentInfo residentInfo = checkedRoom.getResidentInfo();
                Room room = checkedRoom.getRoom();
                String assignedRoom = room != null ? room.getAssignedRoom() : "";

                // 실내 쓰레기방치 화장실 샤워실 보관금지
                //indoor leavedTrash toilet shower prohibitedItem
                row.createCell(0).setCellValue(assignedRoom);
                row.createCell(1).setCellValue(residentInfo == null ? "" : residentInfo.getName());
                row.createCell(2).setCellValue(residentInfo == null ? "" : residentInfo.getPhoneNo());
                row.createCell(3).setCellValue(residentInfo == null ? "" : residentInfo.getStudentId());
                row.createCell(4).setCellValue(checkedRoom.getPassState().getPenalty());
                row.createCell(5).setCellValue(checkedRoom.getPassState().getValue());
                row.createCell(6).setCellValue(checkedRoom.isIndoor() ? "O" : "X");
                row.createCell(7).setCellValue(checkedRoom.isLeavedTrash() ? "O" : "X");
                row.createCell(8).setCellValue(checkedRoom.isToilet() ? "O" : "X");
                row.createCell(9).setCellValue(checkedRoom.isShower() ? "O" : "X");
                row.createCell(10).setCellValue(checkedRoom.isProhibitedItem() ? "O" : "X");
                row.createCell(11).setCellValue(checkedRoom.getEtc() == null ? "" : checkedRoom.getEtc());
            }

            // 파일 내보내기
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_SENT, e);
        }
    }

    // 민원처리내역의 모든 데이터를 엑셀로 반환한다.
    public static void createAndRespondAllDataWitehComplaint(String filename, String sheetName, HttpServletResponse response, List<Complaint> complaints) {
        if (! isExcelExt(filename)) {
            throw new AppServiceException(ErrorCode.INVALID_FILE_EXTENSION);
        }

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

        // try-with-resources를 사용하여 워크북 생성
        try (Workbook workbook = new XSSFWorkbook()) {
            // 새로운 워크시트 생성
            Sheet sheet = workbook.createSheet(sheetName);
            // 헤더 행 작성
            Row headerRow = sheet.createRow(0);
            String[] headers = {"민원번호", "방번호", "민원결과", "민원내역", "민원답변",  "민원인", "민원접수일"};

            for (int i=0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 보기 편하게끔 민원내역 민원답변, 민원접수일 컬럼은 width를 따로 설정
            int columnWidth30 = 30 * 256; // 23문자 너비
            sheet.setColumnWidth(3, columnWidth30);
            sheet.setColumnWidth(4, columnWidth30);
            int columnWidth12 = 12 * 256; // 14문자 너비
            sheet.setColumnWidth(6, columnWidth12);

            // 데이터 작성
            for (int rowNum = 1; rowNum <= complaints.size(); rowNum++) {
                Row row = sheet.createRow(rowNum);

                Complaint complaint = complaints.get(rowNum - 1);

                // "민원번호", "방번호", "처리결과", "민원내역", "민원답변",  "민원인", "민원접수일"
                row.createCell(0).setCellValue(complaint.getId());
                row.createCell(1).setCellValue(complaint.getRoomNo());
                row.createCell(2).setCellValue(complaint.getProcessState().state);
                row.createCell(3).setCellValue(complaint.getComplaintCause());
                row.createCell(4).setCellValue(complaint.getComplaintResolution());
                row.createCell(5).setCellValue(complaint.getName());
                row.createCell(6).setCellValue(complaint.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            // 파일 내보내기
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_SENT, e);
        }
    }
}
