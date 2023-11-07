package com.dominest.dominestbackend.global.exception.exceptions.file;


import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;
import lombok.Getter;

@Getter
public class FileIOException extends AppServiceException {
    public FileIOException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FileIOException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
