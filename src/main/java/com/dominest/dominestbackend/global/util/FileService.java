package com.dominest.dominestbackend.global.util;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.file.FileIOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileService {
    // yml 설정파일
    private final String fileUploadPath;

    public FileService(@Value("${file.upload.path}")String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }

    /**@return save() 메서드 반환값의 리스트*/
    public List<String> save(FilePrefix prefix, List<MultipartFile> multipartFiles){
        List<String> storedFilePaths = new ArrayList<>();
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
        String filenameToStore = convertFileNameToUuid(originalFileName);
        Path filePathToStore = Paths.get(fileUploadPath + prefix.getPrefix() + filenameToStore);

        saveMultipartFile(multipartFile, filePathToStore);
        return filenameToStore;
    }

    public String save(FilePrefix prefix, MultipartFile multipartFile, String filenameToStore){
        // empty Check. type=file 이며 name이 일치한다면, 본문이 비어있어도 MultiPartFile 객체가 생성된다.
        if (multipartFile.isEmpty()) {
            return null;
        }
        Path filePathToStore = Paths.get(fileUploadPath + prefix.getPrefix() + filenameToStore);

        saveMultipartFile(multipartFile, filePathToStore);
        return filenameToStore;
    }

    private void saveMultipartFile(MultipartFile multipartFile, Path filePathToStore) {
        try {
            // transferTo()는 내부적으로 알아서 is, os close를 해준다.
            multipartFile.transferTo(filePathToStore);
        } catch (IOException e) {
            log.error("IOEXCEPTION 발생: originalFile: {}, filePathToStore: {}", multipartFile.getOriginalFilename(), filePathToStore);
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_STORED, e);
        }
    }

    private String convertFileNameToUuid(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFileName);

        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
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

    public void deleteFile(FilePrefix filePrefix, String fileName) {
        String filePathToDelete = fileUploadPath + filePrefix.getPrefix() + fileName;
        Path pathToDelete = Paths.get(filePathToDelete);

        // NotNull 이므로 예외를 발생시키지 않고 바로 빠져나온다.
        // 파일을 찾을 수 없다면 지울 수도 없으므로 작업 취소. DB파일명은 그대로인데 물리적인 파일만 삭제했을 경우를 대비한다.
        if (! Files.exists(pathToDelete))
            return;

        try {
            Files.delete(pathToDelete);
        } catch (IOException e) {
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_DELETED, e);
        }
    }

    public void deleteFile(FilePrefix filePrefix, List<String> fileNames) {
        fileNames.forEach(fileName -> deleteFile(filePrefix, fileName));
    }

    public boolean isInvalidFileExtension(String fileName, FileExt fileExt) {
        String ext = extractExt(fileName);
        return !ext.equals(fileExt.value);
    }

    // fileUploadPath 내부에 저장될 directory 를 선택한다.
    // fileUplaodPath + FilePrefix + fileName 으로 저장된다.
    @Getter
    public enum FilePrefix {
        RESIDENT_ADMISSION("resident/admission/"),
        RESIDENT_DEPARTURE("resident/departure/"),
        POST_IMAGE_TYPE("post/image_type/"),
        NONE(""),
        ;

        private final String prefix;

        FilePrefix(String prefix) {
            this.prefix = prefix;
        }
    }
    @RequiredArgsConstructor
    public enum FileExt {
        PDF("pdf"),
        XLSX("xlsx");

        public final String value;
    }
}











