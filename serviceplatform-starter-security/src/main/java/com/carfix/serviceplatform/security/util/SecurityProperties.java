package com.carfix.serviceplatform.security.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@ConfigurationProperties("serviceplatform.security")
@PropertySources({
        @PropertySource("applicationConfig: [classpath:/application.yaml]"),
        @PropertySource("sign-up-urls property source")
})
public class SecurityProperties {

    private final String tokenHeaderName = "Authorization";
    private final String tokenPrefix = "Bearer ";
    private String[] signUpUrls = new String[]{};
    private final String roleClaim = "ROLE";
    private final String userIdClaim = "userId";
    private final String validFrom = "validFrom";
    private final String userRoleHeaderName = "User-Role";

    private Authentication authentication = new Authentication();

    public static class Authentication {
        private long expirationTime;
        private String secret;

        public long getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public String getTokenHeaderName() {
        return tokenHeaderName;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public String[] getSignUpUrls() {
        return signUpUrls;
    }

    public void setSignUpUrls(String[] signUpUrls) {
        this.signUpUrls = signUpUrls;
    }

    public String getRoleClaim() {
        return roleClaim;
    }

    public String getUserIdClaim() {
        return userIdClaim;
    }

    public String getUserRoleHeaderName() {
        return userRoleHeaderName;
    }

    public String getValidFrom() {
        return validFrom;
    }
}
