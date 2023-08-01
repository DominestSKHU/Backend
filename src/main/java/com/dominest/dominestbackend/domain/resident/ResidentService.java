package com.dominest.dominestbackend.domain.resident;

import com.dominest.dominestbackend.api.resident.dto.ResidentListDto;
import com.dominest.dominestbackend.api.resident.dto.ResidentPdfListDto;
import com.dominest.dominestbackend.domain.resident.component.ResidenceSemester;
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

    /** @return 저장한 파일명 */
    @Transactional
    public String uploadPdf(Long id, FileService.FilePrefix filePrefix, MultipartFile pdf) {
        // 로컬에 파일 저장
        String uploadedFileName = fileService.save(filePrefix, pdf);
        Resident resident = findById(id);
        String prevFileName = resident.getPdfFileName();

        // 파일명 저장 후 반환
        resident.setPdfFileName(uploadedFileName);

        fileService.deleteFile(FileService.FilePrefix.RESIDENT_PDF, prevFileName);
        return uploadedFileName;
    }

    // Todo 무시된 데이터는 클라이언트에 반환하기. 'xxx.pdf' 파일이 변경되었다 식으로.
    /**@return 업로드한 파일 개수*/
    @Transactional
    public int uploadPdfs(FileService.FilePrefix filePrefix, List<MultipartFile> pdfs, ResidenceSemester residenceSemester) {
        int uploadCount = 0;
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
            if (resident == null) continue;

            // 로컬에 파일 저장
            String uploadedFileName = fileService.save(filePrefix, pdf);
            uploadCount++;

            // resident 객체에 파일명 저장
            resident.setPdfFileName(uploadedFileName);
        }
        // 한 건도 업로드하지 못했으면 예외발생
        if (uploadCount == 0)
            throw new BusinessException(ErrorCode.NO_FILE_UPLOADED);
        return uploadCount;
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
        // 현재 서로 다른 차수의 데이터가 존재하지 않는 것이 요구되므로 전체삭제.  TODO 차수 컬럼이 꼭 필요한지 다시 생각해봐야 할 듯
        if (residentRepository.existsByResidenceSemester(residenceSemester)) {
            residentRepository.deleteAllInBatch();
        }
        // 데이터를 저장한다. 예외발생시 삭제나 저장 작업의 트랜잭션 롤백.
        for (List<String> row : sheet) {
            if ("".equals(row.get(columnCount - 1))) // 빈 row 발견 시 continue;
                continue;
            Resident resident = Resident.from(row, residenceSemester);
            saveResident(resident);
        }
    }

    public ResidentListDto.Res getAllResidentByResidenceSemester(ResidenceSemester residenceSemester) {
        List<Resident> residents = residentRepository.findAllByResidenceSemester(residenceSemester);
        return ResidentListDto.Res.from(residents);
    }

    // 테스트용 전체삭제 API
    @Transactional
    public void deleteAllResident() {
        residentRepository.deleteAllInBatch();
    }

    @Transactional
    public void saveResident(Resident resident) {
        try {
            residentRepository.save(resident);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("학생 저장 실패, 잘못된 입력값입니다. 학번 중복 혹은 데이터 형식을 확인해주세요.오류 메시지: " +
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void updateResident(Long id, Resident resident) {
        Resident residentToUpdate = findById(id);
        residentToUpdate.updateValueFrom(resident);
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

    public ResidentPdfListDto.Res getAllPdfs(ResidenceSemester semester) {
        List<Resident> residents = residentRepository.findAllByResidenceSemester(semester);
        return ResidentPdfListDto.Res.from(residents);
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
















