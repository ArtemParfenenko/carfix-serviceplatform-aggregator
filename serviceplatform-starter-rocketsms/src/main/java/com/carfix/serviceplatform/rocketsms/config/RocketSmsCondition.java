package com.carfix.serviceplatform.rocketsms.config;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class RocketSmsCondition extends AllNestedConditions {

    public RocketSmsCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty("serviceplatform.rocketsms.username")
    public static class UsernameCondition {}

    @ConditionalOnProperty("serviceplatform.rocketsms.password")
    public static class PasswordCondition {}
}
