package com.carfix.serviceplatform.security.util;

import com.carfix.serviceplatform.core.exception.ServicePlatformException;
import com.carfix.serviceplatform.security.token.UserRoleAuthenticationToken;
import com.carfix.serviceplatform.security.user.ServicePlatformUserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class RoleSwitcher extends SecurityPropertiesHolder {

    private RoleSwitcher() {
    }

    private static class InstanceHolder {
        private static final RoleSwitcher INSTANCE = new RoleSwitcher();
    }

    public static RoleSwitcher getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public String switchToInternalRole() throws ServicePlatformException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof UserRoleAuthenticationToken)) {
            throw new ServicePlatformException("User has incorrect authentication format. Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        UserRoleAuthenticationToken token = new UserRoleAuthenticationToken(
                authentication, ServicePlatformUserRole.INTERNAL, ((UserRoleAuthenticationToken) authentication).getUserId());
        return JwtCreator.getInstance().createToken(token);
    }
}
