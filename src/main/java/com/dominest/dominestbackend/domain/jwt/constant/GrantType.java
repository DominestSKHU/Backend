package com.dominest.dominestbackend.domain.jwt.constant;

import lombok.Getter;

@Getter
public enum GrantType {

    BEARER("Bearer");

    GrantType(String type) {
        this.type = type;
    }

    private String type;
}
