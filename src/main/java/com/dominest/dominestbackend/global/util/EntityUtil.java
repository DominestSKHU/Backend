package com.dominest.dominestbackend.global.util;


import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;

import java.util.Optional;

public class EntityUtil {
    public static <E> E checkNotFound(E entity, ErrorCode errorCode) {
        if (entity == null) throw new BusinessException(errorCode);
        return entity;
    }

    public static <E> E checkNotFound(Optional<E> entity, ErrorCode errorCode) {
        return entity.orElseThrow(() -> new BusinessException(errorCode));
    }

    public static void mustNull(Object entity, ErrorCode errorCode) {
        if (entity != null) throw new BusinessException(errorCode);
    }


}
