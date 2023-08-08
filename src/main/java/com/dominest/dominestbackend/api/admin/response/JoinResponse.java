package com.dominest.dominestbackend.api.admin.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class JoinResponse {
    private String email;

    private String password;

    public static JoinResponse of(String email, String password){
        return new JoinResponse(email, password);
    }
}
