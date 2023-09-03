package com.dominest.dominestbackend.domain.post.component.category.component;

import com.dominest.dominestbackend.global.exception.ErrorCode;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.Getter;

@Getter
public enum Type {
    IMAGE("image-types"), TEXT_IMAGE("text-image-types")
    , UNDELIVERED_PARCEL_REGISTER("undelivered-parcel-registers")
    ;

    private final String url;

    Type(String url) {
        this.url = url;
    }

    public void validateEqualTo(Type type){
        if (! this.equals(type)){
            throw new BusinessException(ErrorCode.CATEGORY_TYPE_MISMATCHED);
        }
    }
}