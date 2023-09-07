package com.dominest.dominestbackend.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.Principal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrincipalUtil {
    public static String toEmail(Principal principal) {
        return principal.getName().split(":")[0];
    }
    public static String toName(Principal principal) {
        String[] splitedPrincipal = principal.getName().split(":");
        if (splitedPrincipal.length < 2) {
            return "anonymous";
        }
        return splitedPrincipal[1];
    }

    // Principal.toString()의 결과값 ("email:name 형태")을 파싱해서 이메일과 이름을 추출한다.
    public static String strToEmail(String principalStr) {
        return principalStr.split(":")[0];
    }
    public static String strToName(String principalStr) {
        String[] splitedPrincipal = principalStr.split(":");
        if (splitedPrincipal.length < 2) {
            return "anonymous";
        }
        return splitedPrincipal[1];
    }
}
