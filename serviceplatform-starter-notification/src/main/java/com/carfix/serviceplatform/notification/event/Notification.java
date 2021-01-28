package com.carfix.serviceplatform.notification.event;

@FunctionalInterface
public interface Notification {

    void doNotify(Object destination, String message);
}
