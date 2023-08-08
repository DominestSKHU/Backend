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

    public static JoinRequest of(String email, String password){
        return new JoinRequest(email, password);
    }
}