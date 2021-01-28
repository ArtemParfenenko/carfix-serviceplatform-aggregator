package com.carfix.serviceplatform.security.config;

import com.carfix.serviceplatform.security.annotation.handler.AllowForAnnotationHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class ServicePlatformInterceptorConfigurer implements WebMvcConfigurer {

    private final AllowForAnnotationHandlerInterceptor allowForAnnotationHandlerInterceptor;

    public ServicePlatformInterceptorConfigurer(AllowForAnnotationHandlerInterceptor allowForAnnotationHandlerInterceptor) {
        this.allowForAnnotationHandlerInterceptor = allowForAnnotationHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(allowForAnnotationHandlerInterceptor);
    }
}
