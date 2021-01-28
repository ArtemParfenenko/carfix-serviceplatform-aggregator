package com.carfix.serviceplatform.notification.listener;

import com.carfix.serviceplatform.notification.event.Notification;
import com.carfix.serviceplatform.notification.event.NotificationEvent;
import com.carfix.serviceplatform.notification.event.NotificationNameProvider;
import com.carfix.serviceplatform.notification.factory.NotificationEventFactory;
import com.carfix.serviceplatform.notification.validator.NotificationAvailabilityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import java.util.Optional;

public final class NotificationEventListener implements ApplicationListener<NotificationEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationEventListener.class);

    private final NotificationEventFactory notificationEventFactory;
    private final NotificationAvailabilityValidator notificationAvailabilityValidator;

    public NotificationEventListener(NotificationEventFactory notificationEventFactory, NotificationAvailabilityValidator notificationAvailabilityValidator) {
        this.notificationEventFactory = notificationEventFactory;
        this.notificationAvailabilityValidator = notificationAvailabilityValidator;
    }

    @Override
    public void onApplicationEvent(NotificationEvent notificationEvent) {
        if (notificationEvent.isValidatable() && !notificationAvailabilityValidator.validate(notificationEvent.getDestination(), notificationEvent.getNotificationNameProvider())) {
            return;
        }
        NotificationNameProvider notificationNameProvider = notificationEvent.getNotificationNameProvider();
        Object destination = notificationEvent.getDestination();
        String message = notificationEvent.getMessage();
        Optional<? extends Notification> optionalNotification;
        if ((optionalNotification = notificationEventFactory.extractNotification(notificationNameProvider)).isPresent()) {
            optionalNotification.get().doNotify(destination, message);
        } else {
            LOG.warn("Can not find notification with name {}. Skipped", notificationNameProvider.name());
        }
    }
}
