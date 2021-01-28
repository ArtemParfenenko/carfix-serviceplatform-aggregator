package com.carfix.serviceplatform.notification.builder;

import com.carfix.serviceplatform.notification.event.Notification;
import com.carfix.serviceplatform.notification.event.NotificationNameProvider;

import java.util.function.Supplier;

public interface NotificationBuilderAggregator {

    Supplier<? extends Notification> getBuilder(NotificationNameProvider notificationNameProvider);

    NotificationBuilderAggregator putBuilder(NotificationNameProvider notificationNameProvider, Supplier<? extends Notification> notificationBuilder);
}
