package com.dominest.dominestbackend.domain.post.component.category.component;

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
}