package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.api.resident.dto.ExcelUploadDto;
import com.dominest.dominestbackend.api.resident.dto.PdfBulkUploadDto;
import com.dominest.dominestbackend.api.resident.dto.SaveResidentDto;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoom;
import com.dominest.dominestbackend.domain.post.sanitationcheck.floor.checkedroom.CheckedRoomRepository;
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

import javax.persistence.EntityManager;
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
    private final CheckedRoomRepository checkedRoomRepository;

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
    public ExcelUploadDto.Res excelUpload(List<List<String>> sheet, ResidenceSemester residenceSemester) {
        // 첫 3줄 제거 후 유효 데이터만 추출
        sheet.remove(0); sheet.remove(0);sheet.remove(0);

        int originalRow = sheet.size();
        int successRow = 0;

        // 데이터를 저장한다. 예외발생시 삭제나 저장 작업의 트랜잭션 롤백.
        for (List<String> row : sheet) {
            if ("".equals(row.get(ExcelUtil.RESIDENT_COLUMN_COUNT - 1))) // 빈 row 발견 시 continue;
                continue;
            // Room 객체를 찾아서 넣어줘야 함
            String assignedRoom = row.get(11);

            Room room = roomService.getByAssignedRoom(assignedRoom);
            Resident resident = Resident.from(row, residenceSemester, room);

            if (existsByUniqueKey(resident)) {
                // 엑셀 데이터상 중복이 있을 시 로그만 남기고 다음 행으로 넘어간다.
                log.warn("엑셀 데이터 저장 실패. 중복 데이터가 있어 다음으로 넘어감. 이름: {}, 학번: {}, 학기: {}" +
                                ", 방 번호: {}, 방 코드: {}", resident.getName()
                        , resident.getStudentId(), resident.getResidenceSemester()
                        , resident.getRoom().getId(), resident.getRoom().getAssignedRoom());
                continue;
            }

            save(resident);
            successRow++;
        }
        return ExcelUploadDto.Res.of(originalRow, successRow);
    }

    public List<Resident> getAllResidentByResidenceSemesterFetchRoom(ResidenceSemester residenceSemester) {
        return residentRepository.findAllByResidenceSemesterFetchRoom(residenceSemester);
    }

    // 테스트용 전체삭제 API
    @Transactional
    public void deleteAllResident() {
        residentRepository.findAll().forEach(resident -> deleteById(resident.getId()));
    }

    // 단건 등록용
    @Transactional
    public void save(SaveResidentDto.Req reqDto) {
        Room room = roomService.getByAssignedRoom(reqDto.getAssignedRoom());
        Resident resident = reqDto.toEntity(room);

       save(resident);
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
            throw new BusinessException("입사생 정보 변경 실패, 잘못된 입력값입니다. 데이터 누락 혹은 중복을 확인해주세요." +
                    " 지정 학기에 같은 학번을 가졌거나, 같은 방을 사용중인 입사생이 있을 수 있습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public Resident findById(Long id) {
        return residentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.RESIDENT_NOT_FOUND));
    }

    @Transactional
    public void deleteById(Long id) {
        Resident resident = findById(id);
        residentRepository.delete(resident);
    }

    public List<Resident> findAllByResidenceSemester(ResidenceSemester semester) {
        return residentRepository.findAllByResidenceSemester(semester);
    }

    private boolean existsByUniqueKey(Resident resident) {
        return residentRepository.existsByResidenceSemesterAndStudentIdAndPhoneNumberAndName(
                resident.getResidenceSemester(), resident.getStudentId(), resident.getPhoneNumber(), resident.getName());
    }


    private void save(Resident resident) {
        try {
            // CheckedRoom 등 Resident를 참조하는 테이블에 결과를 반영하지 않는다.
            residentRepository.saveAndFlush(resident);
        } catch (DataIntegrityViolationException e) {
            log.error("데이터 저장 실패. 이름: {}, 학번: {}, 학기: {}, 방 번호: {}, 방 코드: {}"
                    , resident.getName(), resident.getStudentId(), resident.getResidenceSemester()
                    , resident.getRoom().getId(), resident.getRoom().getAssignedRoom());
            throw new BusinessException(
                    String.format("입사생 저장 실패, 잘못된 입력값입니다. 데이터 누락 혹은 중복을 확인해주세요. 이름: %s, 학번: %s, 학기: %s, 방 번호: %d, 방 코드: %s"
                            , resident.getName(), resident.getStudentId(), resident.getResidenceSemester()
                            , resident.getRoom().getId(), resident.getRoom().getAssignedRoom())
                    , HttpStatus.BAD_REQUEST);
        }
    }

}
















