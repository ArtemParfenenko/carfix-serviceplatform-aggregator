package com.carfix.serviceplatform.rocketsms.client;

import com.carfix.serviceplatform.core.exception.ServicePlatformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StubRocketSmsClient implements RocketSmsClient {

    private static final Logger LOG = LoggerFactory.getLogger(StubRocketSmsClient.class);

    @Override
    public void sendSMS(String phone, String message) throws ServicePlatformException {
        LOG.info("Stub sms: {}, {}", phone, message);
    }
}
