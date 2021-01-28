package com.carfix.serviceplatform.security.listener;

import com.carfix.serviceplatform.security.util.SecurityProperties;
import com.carfix.serviceplatform.security.util.SecurityPropertiesHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class SecurityPropertiesHolderListener implements ApplicationListener<ContextRefreshedEvent> {

    private final SecurityProperties securityProperties;
    private final SecurityPropertiesHolder[] securityPropertiesHolders;
    private boolean invoked;

    public SecurityPropertiesHolderListener(SecurityProperties securityProperties, SecurityPropertiesHolder... securityPropertiesHolders) {
        this.securityProperties = securityProperties;
        this.securityPropertiesHolders = securityPropertiesHolders;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!invoked) {
            for (SecurityPropertiesHolder securityPropertiesHolder : securityPropertiesHolders) {
                securityPropertiesHolder.setSecurityProperties(securityProperties);
            }
            invoked = true;
        }
    }
}
