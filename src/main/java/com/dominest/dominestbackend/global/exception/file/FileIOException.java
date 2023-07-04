package com.dominest.dominestbackend.global.exception.file;


import com.dominest.dominestbackend.global.exception.BusinessException;
import com.dominest.dominestbackend.global.exception.ErrorCode;

public class FileIOException extends BusinessException {
    public FileIOException(ErrorCode errorCode) {
        super(errorCode);
    }
}
