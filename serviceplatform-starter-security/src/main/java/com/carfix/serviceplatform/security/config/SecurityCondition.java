package com.carfix.serviceplatform.security.config;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SecurityCondition extends AllNestedConditions {

    public SecurityCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty("serviceplatform.security.authentication.expiration-time")
    public static class ExpirationTimeCondition {}

    @ConditionalOnProperty("serviceplatform.security.authentication.secret")
    public static class SecretCondition {}

    @ConditionalOnBean(UserDetailsService.class)
    public static class SecurityPropertiesBeanCondition {}
}
