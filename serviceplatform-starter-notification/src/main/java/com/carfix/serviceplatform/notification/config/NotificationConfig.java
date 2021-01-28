package com.carfix.serviceplatform.notification.config;

import com.carfix.serviceplatform.notification.builder.NotificationBuilderAggregator;
import com.carfix.serviceplatform.notification.factory.NotificationEventFactory;
import com.carfix.serviceplatform.notification.listener.NotificationEventListener;
import com.carfix.serviceplatform.notification.processor.NotificationProcessor;
import com.carfix.serviceplatform.notification.validator.NotificationAvailabilityValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class NotificationConfig {

    @Bean
    public NotificationEventFactory notificationEventFactory(NotificationBuilderAggregator notificationBuilderAggregator) {
        return new NotificationEventFactory(notificationBuilderAggregator);
    }

    @Bean
    public NotificationProcessor notificationProcessor(ApplicationEventPublisher applicationEventPublisher) {
        return new NotificationProcessor(applicationEventPublisher);
    }

    @Bean
    public NotificationEventListener notificationEventListener(NotificationEventFactory notificationEventFactory, NotificationAvailabilityValidator notificationAvailabilityValidator) {
        return new NotificationEventListener(notificationEventFactory, notificationAvailabilityValidator);
    }

    @Bean
    public static NotificationBuilderAggregatorInterceptorBeanFactoryPostProcessor notificationBuilderAggregatorInterceptorBeanFactoryPostProcessor() {
        return new NotificationBuilderAggregatorInterceptorBeanFactoryPostProcessor();
    }

    @Bean
    public static NotificationAvailabilityValidatorInterceptorBeanFactoryPostProcessor notificationSourceValidatorInterceptorBeanFactoryPostProcessor() {
        return new NotificationAvailabilityValidatorInterceptorBeanFactoryPostProcessor();
    }

    @Bean
    @ConditionalOnProperty(value = "serviceplatform.notification.async", havingValue = "true")
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster applicationEventMulticaster = new SimpleApplicationEventMulticaster();
        applicationEventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return applicationEventMulticaster;
    }
}
