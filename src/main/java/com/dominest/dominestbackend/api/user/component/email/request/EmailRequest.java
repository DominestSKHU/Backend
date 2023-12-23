package com.dominest.dominestbackend.api.user.component.email.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class EmailRequest {
    private String email;
    private String code;

    private String newPassword;
}