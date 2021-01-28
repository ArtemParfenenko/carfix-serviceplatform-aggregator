package com.carfix.serviceplatform.security.util;

import com.carfix.serviceplatform.core.exception.ServicePlatformException;
import org.springframework.http.HttpStatus;

public abstract class SecurityPropertiesHolder {

    private SecurityProperties securityProperties;

    protected SecurityPropertiesHolder() {
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        if (this.securityProperties != null) {
            throw new ServicePlatformException("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        this.securityProperties = securityProperties;
    }
}
