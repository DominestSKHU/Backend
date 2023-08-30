package com.dominest.dominestbackend.domain.jwt.constant;

import lombok.Getter;

@Getter
public enum AuthScheme {

    BEARER("Bearer");

    AuthScheme(String type) {
        this.type = type;
    }

    private String type;
}
