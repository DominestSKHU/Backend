package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.api.resident.dto.PdfBulkUploadDto;
import com.dominest.dominestbackend.api.resident.dto.SaveResidentDto;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoomService;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
import com.dominest.dominestbackend.domain.room.Room;
import com.dominest.dominestbackend.domain.room.RoomService;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import com.dominest.dominestbackend.global.exception.exceptions.domain.EntityNotFoundException;
import com.dominest.dominestbackend.global.util.ExcelUtil;
import com.dominest.dominestbackend.global.util.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ResidentService {
    private final ResidentRepository residentRepository;
    private final FileService fileService;
    private final RoomService roomService;
    private final CheckedRoomService checkedRoomService;

    /** @return 저장한 파일명 */
    @Transactional
    public String uploadPdf(Long id, FileService.FilePrefix filePrefix, MultipartFile pdf) {
        Resident resident = findById(id);
        // 로컬에 파일 저장
        String uploadedFileName = fileService.save(filePrefix, pdf);

        String prevFileName = filePrefix.getPdfFileName(resident);
        filePrefix.setPdfFileNameToResident(resident, uploadedFileName);

        if (prevFileName != null)
            fileService.deleteFile(filePrefix, prevFileName);

        return uploadedFileName;
    }

    @Transactional
    public PdfBulkUploadDto.Res uploadPdfs(FileService.FilePrefix filePrefix, List<MultipartFile> pdfs, ResidenceSemester residenceSemester) {
        PdfBulkUploadDto.Res res = new PdfBulkUploadDto.Res();
        for (MultipartFile pdf : pdfs) {
            // 빈 객체면 continue
            if (pdf.isEmpty()) {
                continue;
            }

            String filename = pdf.getOriginalFilename();
            // pdf 확장자가 아니라면 continue
            if (! filename.endsWith(".pdf")){
                continue;
            }

            // 1. 파일명으로 해당 차수의 학생이름을 찾는다. 파일명은 '학생이름.pdf' 여야 한다.
            String residentName = fileService.extractFileNameNoExt(filename);
            Resident resident = residentRepository.findByNameAndResidenceSemester(residentName, residenceSemester);

            // 파일명에 해당하는 학생이 없으면 continue
            if (resident == null) {
                res.addToDtoList(filename, "FAILED", "학생명이 파일명과 일치하지 않습니다.");
                continue;
            }

            // 로컬에 파일 저장
            String uploadedFileName = fileService.save(filePrefix, pdf);

            // filePrefix에 맞는 파일명을 가져온다.
            String prevFileName = filePrefix.getPdfFileName(resident);
            // 파일명을 filePrefix를 단서로 하여(입사신청, 퇴사신청서) Resident에 저장한다.
            filePrefix.setPdfFileNameToResident(resident, uploadedFileName);

            res.addToDtoList(filename, "OK", null);
            res.addSuccessCount();

            if (prevFileName != null)
                fileService.deleteFile(filePrefix, prevFileName);
        }
        // 한 건도 업로드하지 못했으면 예외발생
        if (res.getSuccessCount() == 0)
            throw new BusinessException(ErrorCode.NO_FILE_UPLOADED);
        return res;
    }

    @Transactional
    public void excelUpload(MultipartFile file, ResidenceSemester residenceSemester) {
        // 예상 컬럼 개수
        int columnCount = ExcelUtil.RESIDENT_COLUMN_COUNT;

        // 엑셀 파싱
        List<List<String>> sheet= ExcelUtil.parseExcel(file);

        Integer sheetColumnCount = Optional.ofNullable(sheet.get(0))
                .map(List::size)
                .orElse(0);

        if (sheetColumnCount != columnCount){
            throw new AppServiceException("읽어들인 컬럼 개수가 " +
                    columnCount + "개가 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        // 첫 3줄 제거 후 유효 데이터만 추출
        sheet.remove(0); sheet.remove(0);sheet.remove(0);

        // 지정 차수에 이미 데이터가 있을 경우 전체삭제.
        if (residentRepository.existsByResidenceSemester(residenceSemester)) {
            residentRepository.deleteAllInBatch();
        }

        // 데이터를 저장한다. 예외발생시 삭제나 저장 작업의 트랜잭션 롤백.
        for (List<String> row : sheet) {
            if ("".equals(row.get(columnCount - 1))) // 빈 row 발견 시 continue;
                continue;
            // Room 객체를 찾아서 넣어줘야 함
            String assignedRoom = row.get(11);

            Room room = roomService.getByAssignedRoom(assignedRoom);
            Resident resident = Resident.from(row, residenceSemester, room);
            saveResident(resident);
        }
    }

    public List<Resident> getAllResidentByResidenceSemester(ResidenceSemester residenceSemester) {
        return residentRepository.findAllByResidenceSemester(residenceSemester);
    }

    // 테스트용 전체삭제 API
    @Transactional
    public void deleteAllResident() {
        residentRepository.deleteAllInBatch();
    }

    // Room 업데이트. 단건 등록용
    @Transactional
    public void saveResident(SaveResidentDto.Req reqDto) {
        // 한 테이블에서 모든 차수의 데이터가 있어야 해서 Unique Check는 DB 제약이 아닌
        // Application 단에서 한다. 학기와 학번이 같은 데이터가 있으면 삭제 후 저장(원본 데이터 덮어쓰기)한다.
        Resident existingResident = residentRepository.findByStudentIdAndResidenceSemester(reqDto.getStudentId(), reqDto.getResidenceSemester());
        if (existingResident != null)
            residentRepository.delete(existingResident);

        Room room = roomService.getByAssignedRoom(reqDto.getAssignedRoom());
        Resident resident = reqDto.toEntity(room);

        try {
            // Sequence 방식의 기본 키 생성 전략을 사용할 땐 쓰기지연이 발생하여 트랜잭션이 끝날 때 insert 쿼리가 실행됨.
            // 따라서 메서드 끝(트랜잭션 커밋) 에서 insert 쿼리가 실행되는데, 이 때 catch 블록의 예외처리 범위를 벗어나므로 saveAndFlush()를 사용한다.
            Resident savedResident = residentRepository.saveAndFlush(resident);

            // 새로 등록된 입사생의 배정방을 대상으로 하는 CheckedRoom에 입사생 정보를 반영한다.
            // checkedRoom, Resident, Room 조인해서,
            List<CheckedRoom> checkedRooms = checkedRoomService.findAllByRoomId(savedResident.getRoom().getId());
            checkedRooms.forEach(checkedRoom -> checkedRoom.setResident(savedResident));

        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("입사생 저장 실패, 잘못된 입력값입니다. 데이터 누락 혹은 중복을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void saveResident(Resident resident) {
        // 한 테이블에서 모든 차수의 데이터가 있어야 해서 Unique Check는 DB 제약이 아닌
        // Application 단에서 한다. 학기와 학번이 같은 데이터가 있으면 삭제 후 저장(원본 데이터 덮어쓰기)한다.
        Resident existingResident = residentRepository.findByStudentIdAndResidenceSemester(resident.getStudentId(), resident.getResidenceSemester());
        if (existingResident != null)
            residentRepository.delete(existingResident);

        try {
            // Sequence 방식의 기본 키 생성 전략을 사용할 땐 쓰기지연이 발생하여 트랜잭션이 끝날 때 insert 쿼리가 실행됨.
            // 따라서 메서드 끝(트랜잭션 커밋) 에서 insert 쿼리가 실행되는데, 이 때 catch 블록의 예외처리 범위를 벗어나므로 saveAndFlush()를 사용한다.
            residentRepository.saveAndFlush(resident);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("입사생 저장 실패, 잘못된 입력값입니다. 데이터 누락 혹은 중복을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void updateResident(Long id, SaveResidentDto.Req reqDto) {
        Room room = roomService.getByAssignedRoom(reqDto.getAssignedRoom());
        Resident resident = reqDto.toEntity(room);

        Resident residentToUpdate = findById(id);
        residentToUpdate.updateValueFrom(resident);

        try {
            residentRepository.saveAndFlush(residentToUpdate);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("입사생 정보 변경 실패, 잘못된 입력값입니다. 데이터 누락 혹은 중복을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }

    public Resident findById(Long id) {
        return residentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.RESIDENT_NOT_FOUND));
    }

    @Transactional
    public void deleteById(Long id) {
        Resident resident = findById(id);
        List<CheckedRoom> checkedRooms = checkedRoomService.findAllByResidentId(resident.getId());
        checkedRooms.forEach(cr -> cr.setResident(null));
        residentRepository.delete(resident);
    }

    public List<Resident> getAllPdfs(ResidenceSemester semester) {
        return residentRepository.findAllByResidenceSemester(semester);
    }

//    @Transactional
//    public int uploadPdfZip(MultipartFile pdfZip, ResidenceSemester residenceSemester) {
//        // 1. zip 확장자 검사
//        if (! pdfZip.getOriginalFilename().endsWith(".zip")){
//            throw new BusinessException(ErrorCode.FILE_NOT_ZIP);
//        }
//        // 2. 내부 파일 순회
//        Path destinationPath = fileService.getUploadPath();
//
//        int uploadCount = 0;
//        // 3. zip 파일의 inputStream을 추출해서 내부의 PDF 파일들을 저장
//        try (ZipInputStream zipInputStream = new ZipInputStream(pdfZip.getInputStream(), StandardCharsets.UTF_8)) {
////        try (ZipInputStream zipInputStream = new ZipInputStream(pdfZip.getInputStream(), StandardCharsets.ISO_8859_1)) {
//            ZipEntry entry;
//            while ((entry = zipInputStream.getNextEntry()) != null) {
//                try {
//                    String filename = entry.getName(); // 원본 파일명
//                    // pdf 확장자가 아니라면 continue
//                    if (! filename.endsWith(".pdf")){
//                        continue;
//                    }
//                    // 2. 파일명으로 해당 차수의 학생이름을 찾는다. 파일명은 '학생이름.pdf' 여야 한다.
//                    // 못 찾으면 continue, 찾았으면 저장(개별업로드 로직)
//                    String residentName = fileService.extractFileNameNoExt(filename);
//                    Resident resident = residentRepository.findByNameAndResidenceSemester(residentName, residenceSemester);
//                    Resident resident2 = residentRepository.findByNameAndResidenceSemester("윤현우", residenceSemester);
//
//                    if (resident == null) continue;
//
//                    // 파일명에 해당하는 학생이 있으므로, 이제 파일명을 UUID로 변경하고 Path객체를 생성한다.
//                    //  filename = UUID.randomUUID() + ".pdf";
//                    Path filePath = destinationPath.resolve(filename);
//
//                    fileService.extractFile(zipInputStream, filePath);
//                    uploadCount++;
//                    resident.setPdfFileName(filename);
//                } finally {
//                    zipInputStream.closeEntry();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return uploadCount;
//    }
}
















