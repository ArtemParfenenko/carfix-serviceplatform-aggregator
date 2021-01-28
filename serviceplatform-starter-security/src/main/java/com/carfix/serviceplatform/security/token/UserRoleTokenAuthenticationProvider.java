package com.carfix.serviceplatform.security.token;

import com.carfix.serviceplatform.core.exception.ServicePlatformException;
import com.carfix.serviceplatform.security.user.AuthUser;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRoleTokenAuthenticationProvider extends DaoAuthenticationProvider {

    public UserRoleTokenAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        super.setUserDetailsService(userDetailsService);
        super.setPasswordEncoder(passwordEncoder);
    }

    @Override
    protected final Authentication createSuccessAuthentication(Object principal,
                                                               Authentication authentication, UserDetails user) {
        if (!user.getClass().equals(AuthUser.class)) {
            throw new ServicePlatformException();
        }

        AuthUser authUser = (AuthUser) user;
        return new UserRoleAuthenticationToken(super.createSuccessAuthentication(principal, authentication, user),
                authUser.getUserRole(), authUser.getUserId());
    }
}
