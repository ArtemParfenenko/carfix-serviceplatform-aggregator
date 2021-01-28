package com.carfix.serviceplatform.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public class UserRoleAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String userRole;
    private final UUID userId;

    public UserRoleAuthenticationToken(String username, String password, String userRole, UUID userId) {
        super(username, password);
        this.userRole = userRole;
        this.userId = userId;
    }

    public UserRoleAuthenticationToken(Authentication authentication, String userRole, UUID userId) {
        super(authentication.getPrincipal(), authentication.getCredentials(), authentication.getAuthorities());
        super.setDetails(authentication.getDetails());
        this.userRole = userRole;
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public UUID getUserId() {
        return userId;
    }
}
