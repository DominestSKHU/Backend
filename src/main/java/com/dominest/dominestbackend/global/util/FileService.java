package com.dominest.dominestbackend.global.util;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.file.FileIOException;
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
    private String fileStorePrefix;

    // return fullFilePath
    public List<String> saveAll(List<MultipartFile> multipartFiles){
        List<String> storedFilePaths = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            // empty Check. type=file 이며 name이 일치한다면, 본문이 비어있어도 MultiPartFile 객체가 생성된다.
            if (multipartFile.isEmpty()){
                continue;
            }
            String originalFileName = multipartFile.getOriginalFilename();
            String storedFilePath = createStoredFilePath(originalFileName);

            try {
                multipartFile.transferTo(new File(fileStorePrefix + storedFilePath));
            } catch (IOException e) {
                // FileIOException 발생시키기 전에, IOEXCEPTION 에 대한 로그를 남긴다.
                log.error("IOEXCEPTION: " + originalFileName + " 저장 불가" );
                e.printStackTrace();
                throw new FileIOException(ErrorCode.FILE_CANNOT_BE_STORED);
            }
            storedFilePaths.add(storedFilePath);
        }
        // 저장한 파일의 경로 리스트를 반환한다.
        return storedFilePaths;
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

    public byte[] getByteArr(String fileName) {
        try (InputStream imageStream = new FileInputStream(fileStorePrefix + fileName)) {
            return imageStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_READ);
        }
    }
}
