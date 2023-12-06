package com.dominest.dominestbackend.global.exception.exceptions.domain;

import com.dominest.dominestbackend.domain.common.DomainName;
import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends AppServiceException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EntityNotFoundException(DomainName domainName, long id, HttpStatus httpStatus) {
        super(domainName.name() + "에서 ID: " + id + "를 찾을 수 없습니다.", httpStatus);
    }
}
