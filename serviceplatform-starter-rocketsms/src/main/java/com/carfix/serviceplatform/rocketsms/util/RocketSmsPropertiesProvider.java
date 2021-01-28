package com.carfix.serviceplatform.rocketsms.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("serviceplatform.rocketsms")
public class RocketSmsPropertiesProvider {

    private String username;
    private String password;
    private String sender;

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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
