package com.carfix.serviceplatform.notification.config;

import com.carfix.serviceplatform.notification.builder.DefaultNotificationBuilderAggregator;
import com.carfix.serviceplatform.notification.builder.NotificationBuilderAggregator;
import com.carfix.serviceplatform.notification.builder.StubNotificationBuilderAggregator;

import static com.carfix.serviceplatform.core.QualifierConstants.NOTIFICATION_BUILDER_AGGREGATOR;

public class NotificationBuilderAggregatorInterceptorBeanFactoryPostProcessor extends BeanRegistrationInterceptorBeanFactoryPostProcessor {

    @Override
    protected String getBeanQualifier() {
        return NOTIFICATION_BUILDER_AGGREGATOR;
    }

    @Override
    protected Object getStubBean() {
        return new StubNotificationBuilderAggregator();
    }

    @Override
    protected String[] getAnalyzedBeanClassNames() {
        return new String[] {NotificationBuilderAggregator.class.getName(), DefaultNotificationBuilderAggregator.class.getName()};
    }
}
