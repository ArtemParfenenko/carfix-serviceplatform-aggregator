package com.carfix.serviceplatform.notification.factory;

import com.carfix.serviceplatform.notification.builder.NotificationBuilderAggregator;
import com.carfix.serviceplatform.notification.event.Notification;
import com.carfix.serviceplatform.notification.event.NotificationNameProvider;

import java.util.Optional;
import java.util.function.Supplier;

public final class NotificationEventFactory {

    private final NotificationBuilderAggregator notificationBuilderAggregator;

    public NotificationEventFactory(NotificationBuilderAggregator notificationBuilderAggregator) {
        this.notificationBuilderAggregator = notificationBuilderAggregator;
    }

    public Optional<? extends Notification> extractNotification(NotificationNameProvider notificationNameProvider) {
        return Optional.ofNullable(notificationBuilderAggregator.getBuilder(notificationNameProvider))
                .map(Supplier::get);
    }
}
