package com.carfix.serviceplatform.keycloak.config;

import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Objects;

public class KeycloakSpringBootConfigResolverBeanPostProcessor implements BeanPostProcessor {

    private String configBeanName;

    private final String issuerUri;

    public KeycloakSpringBootConfigResolverBeanPostProcessor(String issuerUri) {
        this.issuerUri = issuerUri;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (Objects.equals(bean.getClass().getSimpleName(), KeycloakSpringBootConfigResolver.class.getSimpleName())) {
            this.configBeanName = beanName;
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (Objects.equals(beanName, configBeanName) && Objects.nonNull(issuerUri)) {
            return Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(),
                    (proxy, method, args) -> {
                        Object realMethodReturn = method.invoke(bean, args);
                        if (Objects.equals(method.getName(), "resolve")) {
                            if (!Objects.equals(realMethodReturn.getClass(), KeycloakDeployment.class)) {
                                return realMethodReturn;
                            } else {
                                KeycloakDeployment proxyDeployment = createKeycloakDeploymentProxy((KeycloakDeployment) realMethodReturn);
                                Field keycloakDeploymentField = KeycloakSpringBootConfigResolver.class
                                        .getDeclaredField("keycloakDeployment");
                                keycloakDeploymentField.setAccessible(true);
                                keycloakDeploymentField.set(bean, proxyDeployment);
                                keycloakDeploymentField.setAccessible(false);
                                return proxyDeployment;
                            }
                        } else {
                            return realMethodReturn;
                        }
                    });
        } else {
            return bean;
        }
    }

    private KeycloakDeployment createKeycloakDeploymentProxy(KeycloakDeployment originalDeployment) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(KeycloakDeployment.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            Object realMethodReturn = method.invoke(originalDeployment, args);
            if (!Objects.equals(method.getName(), "getRealmInfoUrl")) {
                return realMethodReturn;
            } else {
                return issuerUri;
            }
        });
        return (KeycloakDeployment) enhancer.create();
    }
}
