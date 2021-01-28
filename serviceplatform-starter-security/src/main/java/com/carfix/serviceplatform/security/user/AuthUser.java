package com.carfix.serviceplatform.security.user;

import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.UUID;

public class AuthUser extends User {

    private String userRole;
    private UUID userId;

    public AuthUser(String username, String password, String userRole, UUID userId) {
        super(username, password, Collections.emptyList());
        this.userRole = userRole;
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return getUsername();
    }
}
