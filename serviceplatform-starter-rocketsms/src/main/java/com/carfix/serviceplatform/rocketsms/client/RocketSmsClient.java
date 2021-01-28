package com.carfix.serviceplatform.rocketsms.client;

import com.carfix.serviceplatform.core.exception.ServicePlatformException;

public interface RocketSmsClient {

    void sendSMS(String phone, String message) throws ServicePlatformException;
}
