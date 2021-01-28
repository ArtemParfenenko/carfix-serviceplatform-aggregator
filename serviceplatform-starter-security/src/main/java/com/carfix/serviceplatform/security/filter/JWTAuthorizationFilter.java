package com.carfix.serviceplatform.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.carfix.serviceplatform.security.token.UserRoleAuthenticationToken;
import com.carfix.serviceplatform.security.util.SecurityProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityProperties securityProperties;

    public JWTAuthorizationFilter(AuthenticationManager authManager, SecurityProperties securityProperties) {
        super(authManager != null
                ? authManager
                : authentication -> authentication
        );
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String tokenHeader = request.getHeader(securityProperties.getTokenHeaderName());

        if (tokenHeader == null || !tokenHeader.startsWith(securityProperties.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        UserRoleAuthenticationToken authentication = getAuthentication(tokenHeader);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UserRoleAuthenticationToken getAuthentication(String tokenHeader) {
        DecodedJWT jwtToken = JWT.require(Algorithm.HMAC512(securityProperties.getAuthentication().getSecret().getBytes()))
                .build()
                .verify(tokenHeader.replace(securityProperties.getTokenPrefix(), ""));

        String username = jwtToken.getSubject();

        String userRole = jwtToken.getClaim(securityProperties.getRoleClaim()).asString();
        UUID userId = UUID.fromString(jwtToken.getClaim(securityProperties.getUserIdClaim()).asString());

        if (username != null) {
            return new UserRoleAuthenticationToken(new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()),
                    userRole, userId);
        }
        return null;
    }
}
