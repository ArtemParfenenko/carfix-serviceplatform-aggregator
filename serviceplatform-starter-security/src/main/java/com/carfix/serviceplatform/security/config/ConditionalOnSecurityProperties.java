package com.carfix.serviceplatform.security.config;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(SecurityCondition.class)
@interface ConditionalOnSecurityProperties {
}
