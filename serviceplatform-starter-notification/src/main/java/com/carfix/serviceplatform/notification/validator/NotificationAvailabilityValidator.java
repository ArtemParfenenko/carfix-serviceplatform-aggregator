package com.carfix.serviceplatform.notification.validator;

import com.carfix.serviceplatform.notification.event.NotificationNameProvider;

public interface NotificationAvailabilityValidator {

    boolean validate(Object destination, NotificationNameProvider notificationName);
}
