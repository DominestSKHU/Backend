package com.dominest.dominestbackend.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.Principal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrincipalUtil {
    public static String getEmail(Principal principal) {
        return principal.getName().split(":")[0];
    }
    public static String getName(Principal principal) {
        return principal.getName().split(":")[1];
    }
}
