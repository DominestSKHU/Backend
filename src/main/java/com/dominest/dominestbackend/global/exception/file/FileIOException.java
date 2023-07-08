package com.dominest.dominestbackend.global.exception.file;


import com.dominest.dominestbackend.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileIOException extends RuntimeException {
    private final int statusCode;
    private final HttpStatus httpStatus;
    public FileIOException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.statusCode = errorCode.getStatus();
        this.httpStatus = HttpStatus.valueOf(errorCode.getStatus());
    }
}
