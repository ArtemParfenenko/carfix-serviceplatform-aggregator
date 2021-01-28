package com.carfix.serviceplatform.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.carfix.serviceplatform.security.token.UserRoleAuthenticationToken;

import java.util.Date;

public final class JwtCreator extends SecurityPropertiesHolder {

    private JwtCreator() {
    }

    private static class InstanceHolder {
        private static final JwtCreator INSTANCE = new JwtCreator();
    }

    public static JwtCreator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public String createToken(UserRoleAuthenticationToken authentication) {
        SecurityProperties securityProperties = getSecurityProperties();
        long currentTime = System.currentTimeMillis();
        return JWT.create()
                .withSubject(authentication.getPrincipal().toString())
                .withClaim(securityProperties.getRoleClaim(), authentication.getUserRole())
                .withClaim(securityProperties.getUserIdClaim(), authentication.getUserId().toString())
                .withClaim(securityProperties.getValidFrom(), new Date(currentTime))
                .withExpiresAt(new Date(currentTime + securityProperties.getAuthentication().getExpirationTime()))
                .sign(Algorithm.HMAC512(securityProperties.getAuthentication().getSecret().getBytes()));
    }
}
