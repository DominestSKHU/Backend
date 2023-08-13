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

    private String name;

    private String phoneNumber;

    public static JoinResponse of(String email, String name, String phoneNumber){
        return new JoinResponse(email, name, phoneNumber);
    }

}
