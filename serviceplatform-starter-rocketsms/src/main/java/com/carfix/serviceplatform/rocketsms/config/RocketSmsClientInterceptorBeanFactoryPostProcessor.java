package com.carfix.serviceplatform.rocketsms.config;

import com.carfix.serviceplatform.core.QualifierConstants;
import com.carfix.serviceplatform.rocketsms.client.RocketSmsClient;
import com.carfix.serviceplatform.rocketsms.client.StubRocketSmsClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Arrays;

public class RocketSmsClientInterceptorBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        if (!Arrays.asList(configurableListableBeanFactory.getBeanDefinitionNames())
                .contains(QualifierConstants.ROCKET_SMS_CLIENT)) {
            RocketSmsClient rocketSmsClient = new StubRocketSmsClient();
            configurableListableBeanFactory.registerSingleton(QualifierConstants.ROCKET_SMS_CLIENT, rocketSmsClient);
        }
    }
}
