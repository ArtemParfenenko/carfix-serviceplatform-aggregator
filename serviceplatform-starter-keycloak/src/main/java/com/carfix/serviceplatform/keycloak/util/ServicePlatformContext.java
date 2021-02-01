package com.carfix.serviceplatform.keycloak.util;

import com.carfix.serviceplatform.core.exception.ServicePlatformException;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.UUID;

public final class ServicePlatformContext {

    private ServicePlatformContext() {
    }

    public static UUID getCurrentUserId() throws ServicePlatformException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (!(authentication instanceof KeycloakAuthenticationToken)) {
            throw new ServicePlatformException();
        }
        String idAsString = ((KeycloakAuthenticationToken) authentication)
                .getAccount()
                .getKeycloakSecurityContext()
                .getToken()
                .getSubject();
        return UUID.fromString(idAsString);
    }

    public static Set<String> getCurrentUserRoles() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (!(authentication instanceof KeycloakAuthenticationToken)) {
            throw new ServicePlatformException();
        }
        return ((SimpleKeycloakAccount) authentication.getDetails())
                .getRoles();
    }

    public static String getSingleUserRole() {
        final Set<String> currentUserRoles = getCurrentUserRoles();
        if (currentUserRoles.size() != 1) {
            throw new ServicePlatformException("Current user role is not single", HttpStatus.FORBIDDEN);
        }
        return currentUserRoles.iterator().next();
    }
}
