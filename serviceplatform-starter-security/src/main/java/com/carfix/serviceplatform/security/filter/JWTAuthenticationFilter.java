package com.carfix.serviceplatform.security.filter;

import com.carfix.serviceplatform.core.exception.ServicePlatformException;
import com.carfix.serviceplatform.security.token.UserRoleAuthenticationToken;
import com.carfix.serviceplatform.security.util.JwtCreator;
import com.carfix.serviceplatform.security.util.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final SecurityProperties securityProperties;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   SecurityProperties securityProperties) {
        this.authenticationManager = authenticationManager;
        this.securityProperties = securityProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UsernamePasswordHolder user = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernamePasswordHolder.class);

            return authenticationManager.authenticate(
                    new UserRoleAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            null,
                            null
                    )
            );
        } catch (IOException e) {
            throw new ServicePlatformException(e);
        }
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                         FilterChain filterChain, Authentication authentication) throws IOException {
        String token = JwtCreator.getInstance().createToken((UserRoleAuthenticationToken) authentication);
        response.addHeader(securityProperties.getTokenHeaderName(), securityProperties.getTokenPrefix() + token);
        response.getWriter().append(token);
    }

    private static class UsernamePasswordHolder {
        private String username;
        private String password;

        public UsernamePasswordHolder() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
