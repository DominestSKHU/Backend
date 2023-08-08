package com.dominest.dominestbackend.api.admin.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ChangePasswordRequest {

    private String password;

    private String newPassword;
}
