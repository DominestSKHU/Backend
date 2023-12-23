package com.dominest.dominestbackend.api.user.component.email.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CodeRequest {
    private String code;
}