package com.dominest.dominestbackend.global.util;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.file.FileIOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileService {
    @Value("${file.upload.path}")// yml 설정파일
    private String fileUploadPath;

    // return fullFilePath
    public List<String> save(FilePrefix prefix, List<MultipartFile> multipartFiles){
        List<String> storedFilePaths = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String storedFilePath = save(prefix, multipartFile);
            if (storedFilePath == null) continue;
            storedFilePaths.add(storedFilePath);
        }
        // 저장한 파일의 경로 리스트를 반환한다.
        return storedFilePaths;
    }

    /**
     * @return "prefix" + "저장된 파일명 UUID" + ".확장자"
     */
    // return fullFilePath
    public String save(FilePrefix prefix, MultipartFile multipartFile){
        // empty Check. type=file 이며 name이 일치한다면, 본문이 비어있어도 MultiPartFile 객체가 생성된다.
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFileName = multipartFile.getOriginalFilename();
        String storedFilePath = prefix.getPrefix() + createStoredFilePath(originalFileName);

        try {
            multipartFile.transferTo(new File(fileUploadPath + storedFilePath));
        } catch (IOException e) {
            // FileIOException 발생시키기 전에, IOEXCEPTION 에 대한 로그를 남긴다.
            log.error("IOEXCEPTION: " + originalFileName + " 저장 불가" );
            e.printStackTrace();
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_STORED);
        }
        // 저장한 파일의 경로 리스트를 반환한다.
        return storedFilePath;
    }

    private String createStoredFilePath(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFileName);

        return uuid + "." + ext;
    }

    private String createStoredFilePath(String originalFileName, String prefix) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFileName);

        return prefix + "/" + uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos +1);
    }

    public byte[] getByteArr(FilePrefix filePrefix, String fileName) {
        String fullFilePath = filePrefix.getPrefix() + fileName;
        try (InputStream imageStream = new FileInputStream(fileUploadPath + fullFilePath)) {
            return imageStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_READ);
        }
    }

    // fileUploadPath 내부에 저장될 directory 를 선택한다.
    // fileUplaodPath + FilePrefix + fileName 으로 저장된다.
    @Getter
    public enum FilePrefix {
        RESIDENT_PDF("resident/pdf/"),
        NONE(""),
        ;

        private final String prefix;

        FilePrefix(String prefix) {
            this.prefix = prefix;
        }
    }
}
