package com.carfix.serviceplatform.notification.config;

import com.carfix.serviceplatform.notification.validator.NotificationAvailabilityValidator;

import static com.carfix.serviceplatform.core.QualifierConstants.NOTIFICATION_SOURCE_VALIDATOR;

public class NotificationAvailabilityValidatorInterceptorBeanFactoryPostProcessor extends BeanRegistrationInterceptorBeanFactoryPostProcessor {

    @Override
    protected String getBeanQualifier() {
        return NOTIFICATION_SOURCE_VALIDATOR;
    }

    @Override
    protected Object getStubBean() {
        return (NotificationAvailabilityValidator) (source, notificationName) -> true;
    }

    @Override
    protected String[] getAnalyzedBeanClassNames() {
        return new String[] {NotificationAvailabilityValidator.class.getName()};
    }
}
