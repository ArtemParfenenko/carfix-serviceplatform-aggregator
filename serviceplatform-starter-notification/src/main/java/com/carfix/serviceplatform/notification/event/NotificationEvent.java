package com.carfix.serviceplatform.notification.event;

import org.springframework.context.ApplicationEvent;

public class NotificationEvent extends ApplicationEvent {

    private final NotificationNameProvider notificationNameProvider;
    private final Object destination;
    private final String message;
    private final boolean validatable;

    public NotificationEvent(NotificationNameProvider notificationNameProvider, Object destination, String message) {
        super(notificationNameProvider.name());
        this.notificationNameProvider = notificationNameProvider;
        this.destination = destination;
        this.message = message;
        this.validatable = false;
    }

    public NotificationEvent(NotificationNameProvider notificationNameProvider, Object destination, String message, boolean validatable) {
        super(notificationNameProvider.name());
        this.notificationNameProvider = notificationNameProvider;
        this.destination = destination;
        this.message = message;
        this.validatable = validatable;
    }

    public NotificationNameProvider getNotificationNameProvider() {
        return notificationNameProvider;
    }

    public Object getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }

    public boolean isValidatable() {
        return validatable;
    }
}
