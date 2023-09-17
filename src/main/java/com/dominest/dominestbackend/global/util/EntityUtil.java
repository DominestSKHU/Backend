package com.dominest.dominestbackend.global.util;


import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityUtil {
    public static <T> T mustNotNull(T entity, ErrorCode errorCode) {
        if (entity == null) throw new BusinessException(errorCode);
        return entity;
    }

    public static <T> T mustNotNull(T entity, String errorMsg, HttpStatus httpStatus) {
        if (entity == null) throw new BusinessException(errorMsg, httpStatus);
        return entity;
    }

    public static <T> T mustNotNull(Optional<T> optEntity, ErrorCode errorCode) {
        return optEntity.orElseThrow(() -> new BusinessException(errorCode));
    }

}
