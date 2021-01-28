package com.carfix.serviceplatform.security.util;

import com.carfix.serviceplatform.core.exception.ServicePlatformException;
import com.carfix.serviceplatform.security.token.UserRoleAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class ServicePlatformContext {

    private ServicePlatformContext() {
    }

    public static UUID getCurrentUserId() throws ServicePlatformException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserRoleAuthenticationToken authentication = (UserRoleAuthenticationToken) securityContext.getAuthentication();
        if (authentication == null || authentication.getUserId() == null) {
            throw new ServicePlatformException();
        }
        return authentication.getUserId();
    }

    public static String getCurrentUserRole() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserRoleAuthenticationToken authentication = (UserRoleAuthenticationToken) securityContext.getAuthentication();
        if (authentication == null || authentication.getUserRole() == null) {
            throw new ServicePlatformException();
        }
        return authentication.getUserRole();
    }
}
