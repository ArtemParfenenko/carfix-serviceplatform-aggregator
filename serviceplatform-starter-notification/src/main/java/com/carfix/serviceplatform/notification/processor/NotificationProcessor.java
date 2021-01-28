package com.carfix.serviceplatform.notification.processor;

import com.carfix.serviceplatform.notification.event.NotificationEvent;
import com.carfix.serviceplatform.notification.event.NotificationNameProvider;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Objects;

public final class NotificationProcessor {

    private final ApplicationEventPublisher applicationEventPublisher;

    public NotificationProcessor(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void processNotification(NotificationNameProvider notificationNameProvider, Object destination, String message) {
        Objects.requireNonNull(notificationNameProvider);
        Objects.requireNonNull(destination);
        Objects.requireNonNull(message);
        applicationEventPublisher.publishEvent(new NotificationEvent(notificationNameProvider, destination, message));
    }

    public void processValidNotification(NotificationNameProvider notificationNameProvider, Object destination, String message) {
        Objects.requireNonNull(notificationNameProvider);
        Objects.requireNonNull(destination);
        Objects.requireNonNull(message);
        applicationEventPublisher.publishEvent(new NotificationEvent(notificationNameProvider, destination, message, true));
    }
}
