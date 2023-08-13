package com.dominest.dominestbackend.domain.role;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER("User"),
    ROLE_ADMIN("Admin");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getAuthority() {
        return description;
    }
}