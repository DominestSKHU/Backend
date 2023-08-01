package com.dominest.dominestbackend.global.util;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.file.FileIOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileService {
    @Value("${file.upload.path}")// yml 설정파일
    private String fileUploadPath;

    /**@return save() 메서드 반환값의 리스트*/
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
            // FileIOException 발생시키기 전에, IOEXCEPTION 에 대한 로그를 남긴다.
            log.error("IOEXCEPTION: " + originalFileName + " 저장 불가" );
            e.printStackTrace();
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_STORED);
        }

        return storedFileName;
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
        try (InputStream imageStream = new FileInputStream(fileUploadPath + fullFilePath)) {
            return imageStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_READ);
        }
    }

    public void deleteFile(FilePrefix filePrefix, String prevFileName) {
        String filePathToDelete = fileUploadPath + filePrefix.getPrefix() + prevFileName;
        Path pathToDelete = Paths.get(filePathToDelete);

        if (! Files.exists(pathToDelete))
            throw new FileIOException(ErrorCode.FILE_NOT_FOUND);

        try {
            Files.delete(pathToDelete);
        } catch (IOException e) {
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_DELETED);
        }


    }

    // zipInputStream을 받아서 Path에 저장한다.
//    public void extractFile(ZipInputStream zipInputStream, Path filePath) throws IOException {
//        try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(filePath))) {
//            byte[] bytesIn = new byte[4096];
//            int read;
//            while ((read = zipInputStream.read(bytesIn)) != -1) { // 끝까지 읽었을 경우 -1 반환, 끝이 아닐 경우 읽은 바이트 수 반환
//                bos.write(bytesIn, 0, read);                // Is에서 읽어들인 byte배열의 length의 크기만큼 0부터 read까지 쓴다.
//            }
//        }
//    }

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
