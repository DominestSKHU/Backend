package com.dominest.dominestbackend.global.util;

import com.dominest.dominestbackend.domain.resident.Resident;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import com.dominest.dominestbackend.global.exception.exceptions.file.FileIOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
public class FileService {
    @Value("${file.upload.path}")// yml 설정파일
    private String fileUploadPath;

    /**@return save() 메서드 반환값의 리스트*/
    public List<String> save(FilePrefix prefix, List<MultipartFile> multipartFiles){
        List<String> storedFilePaths = new ArrayList<>();
        if(multipartFiles == null ) return storedFilePaths;
        for (MultipartFile multipartFile : multipartFiles) {
            String storedFilePath = save(prefix, multipartFile);
            if (storedFilePath == null) {
                log.warn("save() 메서드 null 반환, 파일이 비어있을 수 있음.");
                continue;
            }
            storedFilePaths.add(storedFilePath);
        }
        // 저장한 파일의 경로 리스트를 반환한다.
        return storedFilePaths;
    }

    /**@return "저장된 파일명 UUID" + ".확장자". */
    public String save(FilePrefix prefix, MultipartFile multipartFile){
        // empty Check. type=file 이며 name이 일치한다면, 본문이 비어있어도 MultiPartFile 객체가 생성된다.
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFileName = multipartFile.getOriginalFilename();
        String storedFileName = createStoredFilePath(originalFileName);
        Path storedFilePath = Paths.get(fileUploadPath + prefix.getPrefix() + storedFileName);


        try {
            // transferTo()는 내부적으로 알아서 is, os close를 해준다.
            multipartFile.transferTo(storedFilePath);
        } catch (IOException e) {
            log.error("IOEXCEPTION 발생: originalFile: {}, storedFilePath: {}", originalFileName, storedFilePath.toString());
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_STORED, e);
        }

        return storedFileName;
    }

    //일단은 원래 함수를 보존하기 위해서 코드 구조가 매우 유사하지만 코드를 따로 만들었음.
    public Set<String> save(FilePrefix prefix, String subPath, Set<MultipartFile> multipartFiles){
        Set<String> storedFilePaths = new HashSet<>();
        if(multipartFiles == null ) return storedFilePaths;
        for (MultipartFile multipartFile : multipartFiles) {
            String storedFilePath = save(prefix, subPath, multipartFile);
            if (storedFilePath == null) {
                log.warn("save() 메서드 null 반환, 파일이 비어있을 수 있음.");
                continue;
            }
            storedFilePaths.add(storedFilePath);
        }
        // 저장한 파일의 경로 리스트를 반환한다.
        return storedFilePaths;
    }

    /**@return "저장된 파일명 UUID" + ".확장자". */
    public String save(FilePrefix prefix, String subPath, MultipartFile multipartFile){
        // empty Check. type=file 이며 name이 일치한다면, 본문이 비어있어도 MultiPartFile 객체가 생성된다.
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFileName = multipartFile.getOriginalFilename();
        String storedFileFolderStr = fileUploadPath + subPath + prefix.getPrefix(); // 절대경로
        String storedFileStr = storedFileFolderStr + originalFileName; // 절대경로
        String storedReletiveFileStr = subPath + prefix.getPrefix() + originalFileName; // 상대경로
        Path storedFilePath = Paths.get(storedFileStr);

        File folder = new File(storedFileFolderStr);
        try {
            //폴더가 없다면 자동 생성
            if(!folder.exists()) {
                folder.mkdir();
            }
            // transferTo()는 내부적으로 알아서 is, os close를 해준다.
            multipartFile.transferTo(storedFilePath);
        } catch (IOException e) {
            log.error("IOEXCEPTION 발생: originalFile: {}, storedFilePath: {}", originalFileName, storedFilePath.toString());
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_STORED, e);
        }

        return storedReletiveFileStr;
    }

    private String createStoredFilePath(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFileName);

        return uuid + "." + ext;
    }

    public String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos +1);
    }

    public String extractFileNameNoExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        if (pos > 0) {
            return originalFileName.substring(0, pos);
        } else {
            return originalFileName;
        }
    }

    public byte[] getByteArr(FilePrefix filePrefix, String fileName) {
        String fullFilePath = filePrefix.getPrefix() + fileName;
        try  {
            Path path = Paths.get(fileUploadPath + fullFilePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("IOEXCEPTION 발생: filePrefix: {}, fileName: {}", filePrefix, fileName);
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_READ, e);
        }
    }

    public byte[] getByteArr(String filePath) {
        try  {
            Path path = Paths.get(fileUploadPath + filePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("IOEXCEPTION 발생: filePath: {}", filePath);
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_READ, e);
        }
    }

    public void deleteFile(FilePrefix filePrefix, String fileName) {
        deleteFile(filePrefix.getPrefix()+fileName);
        /*String filePathToDelete = fileUploadPath + filePrefix.getPrefix() + fileName;
        Path pathToDelete = Paths.get(filePathToDelete);

        // NotNull 이므로 예외를 발생시키지 않고 바로 빠져나온다.
        // 파일을 찾을 수 없다면 지울 수도 없으므로 작업 취소. DB파일명은 그대로인데 물리적인 파일만 삭제했을 경우를 대비한다.
        if (! Files.exists(pathToDelete))
            return;

        try {
            Files.delete(pathToDelete);
        } catch (IOException e) {
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_DELETED, e);
        }*/
    }

    public void deleteFile(String filePath) {
        String filePathToDelete = fileUploadPath + filePath;
        Path pathToDelete = Paths.get(filePathToDelete);

        // NotNull 이므로 예외를 발생시키지 않고 바로 빠져나온다.
        // 파일을 찾을 수 없다면 지울 수도 없으므로 작업 취소. DB파일명은 그대로인데 물리적인 파일만 삭제했을 경우를 대비한다.

        if(!Files.exists(pathToDelete)) return;
        try {
            Files.delete(pathToDelete);
        } catch(IOException e) {
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_DELETED, e);
        }
    }

    public void deleteFile(FilePrefix filePrefix, Collection<String> fileNames) {
        fileNames.forEach(fileName -> deleteFile(filePrefix, fileName));
    }


    // fileUploadPath 내부에 저장될 directory 를 선택한다.
    // fileUplaodPath + FilePrefix + fileName 으로 저장된다.
    @Getter
    public enum FilePrefix {
        RESIDENT_ADMISSION("resident/admission/"),
        RESIDENT_DEPARTURE("resident/departure/"),
        POST_IMAGE_TYPE("post/image_type/"),

        ATTACH_TYPE("attach/"),
        IMAGE_TYPE("image/"),
        VIDEO_TYPE("video/"),
        NONE(""),
        ;

        private final String prefix;

        FilePrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPdfFileName(Resident resident) {
            if (this.equals(RESIDENT_ADMISSION)) {
                return resident.getAdmissionPdfFileName();
            } else if (this.equals(RESIDENT_DEPARTURE)) {
                return resident.getDeparturePdfFileName();
            } else {
                return null;
            }
        }

        public void setPdfFileNameToResident(Resident resident, String uploadedFileName) {
            if (this.equals(RESIDENT_ADMISSION)) {
                resident.setAdmissionPdfFileName(uploadedFileName);
            } else if (this.equals(RESIDENT_DEPARTURE)) {
                resident.setDeparturePdfFileName(uploadedFileName);
            } else { // 입사신청서, 퇴사신청서가 아닌 다른 FilePrefix 값일 때
                throw new BusinessException("잘못된 FilePrefix 값입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}