package com.carfix.serviceplatform.notification.builder;

import com.carfix.serviceplatform.notification.event.Notification;
import com.carfix.serviceplatform.notification.event.NotificationNameProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class DefaultNotificationBuilderAggregator implements NotificationBuilderAggregator {

    private final Map<String, Supplier<? extends Notification>> notificationBuilderMap = new HashMap<>();

    public DefaultNotificationBuilderAggregator() {
    }

    public Supplier<? extends Notification> getBuilder(NotificationNameProvider notificationNameProvider) {
        return notificationBuilderMap.get(notificationNameProvider.name());
    }

    public DefaultNotificationBuilderAggregator putBuilder(NotificationNameProvider notificationNameProvider,
                                                           Supplier<? extends Notification> notificationBuilder) {
        notificationBuilderMap.put(notificationNameProvider.name(), notificationBuilder);
        return this;
    }
}
