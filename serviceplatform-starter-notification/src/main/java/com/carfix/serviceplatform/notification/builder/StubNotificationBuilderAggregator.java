package com.carfix.serviceplatform.notification.builder;

import com.carfix.serviceplatform.notification.event.Notification;
import com.carfix.serviceplatform.notification.event.NotificationNameProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class StubNotificationBuilderAggregator implements NotificationBuilderAggregator {

    private static final Logger LOG = LoggerFactory.getLogger(StubNotificationBuilderAggregator.class);

    @Override
    public Supplier<? extends Notification> getBuilder(NotificationNameProvider notificationNameProvider) {
        LOG.debug("Get stub notification builder by notification name '{}'. Skipped", notificationNameProvider.name());
        return null;
    }

    @Override
    public NotificationBuilderAggregator putBuilder(NotificationNameProvider notificationNameProvider,
                                                    Supplier<? extends Notification> notificationBuilder) {
        LOG.debug("Put stub notification builder with notification name '{}'. Skipped", notificationNameProvider.name());
        return this;
    }
}
