package com.dominest.dominestbackend.api.admin.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class JoinRequest {
    private String email;

    private String password;

    private String name;

    private String phoneNumber;
}