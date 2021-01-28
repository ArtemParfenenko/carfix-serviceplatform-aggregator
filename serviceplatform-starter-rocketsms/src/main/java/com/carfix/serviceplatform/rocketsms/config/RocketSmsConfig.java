package com.carfix.serviceplatform.rocketsms.config;

import com.carfix.serviceplatform.core.QualifierConstants;
import com.carfix.serviceplatform.rocketsms.client.DefaultRocketSmsClient;
import com.carfix.serviceplatform.rocketsms.client.RocketSmsClient;
import com.carfix.serviceplatform.rocketsms.util.RocketSmsPropertiesProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(RocketSmsPropertiesProvider.class)
public class RocketSmsConfig {

    @Bean(QualifierConstants.ROCKET_SMS_REST_TEMPLATE)
    @ConditionalOnRocketSmsProperties
    public RestTemplate rocketSmsRestTemplate() {
        return new RestTemplate();
    }

    @Bean(QualifierConstants.ROCKET_SMS_CLIENT)
    @ConditionalOnRocketSmsProperties
    public RocketSmsClient rocketSmsClient(RestTemplate restTemplate, RocketSmsPropertiesProvider propertiesProvider) {
        return new DefaultRocketSmsClient(restTemplate, propertiesProvider);
    }

    @Bean
    public static RocketSmsClientInterceptorBeanFactoryPostProcessor rocketSmsClientInterceptorBeanFactoryPostProcessor() {
        return new RocketSmsClientInterceptorBeanFactoryPostProcessor();
    }
}
