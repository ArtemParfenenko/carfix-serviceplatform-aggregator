package com.carfix.serviceplatform.notification.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

import java.util.Arrays;

public abstract class BeanRegistrationInterceptorBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        if (Arrays.stream(configurableListableBeanFactory.getBeanDefinitionNames())
                .map(configurableListableBeanFactory::getBeanDefinition)
                .noneMatch(beanDefinition -> validateBeanDefinition(beanDefinition, configurableListableBeanFactory))) {
            configurableListableBeanFactory.registerSingleton(getBeanQualifier(), getStubBean());
        }
    }

    protected abstract String getBeanQualifier();

    protected abstract Object getStubBean();

    protected abstract String[] getAnalyzedBeanClassNames();

    private boolean validateBeanDefinition(BeanDefinition beanDefinition, ConfigurableListableBeanFactory beanFactory) {
        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            MethodMetadata factoryMethodMetadata = ((AnnotatedBeanDefinition) beanDefinition).getFactoryMethodMetadata();
            AnnotationMetadata metadata = ((AnnotatedBeanDefinition) beanDefinition).getMetadata();
            if (factoryMethodMetadata != null && validateStringOnEquality(factoryMethodMetadata.getReturnTypeName(), getAnalyzedBeanClassNames())) {
                return true;
            }
            return metadata != null && validateStringOnEquality(metadata.getClassName(), getAnalyzedBeanClassNames());
        } else {
            String beanClassName = beanDefinition.getBeanClassName();
            BeanDefinition parentBeanDefinition = beanDefinition;
            while (beanClassName == null) {
                parentBeanDefinition = beanFactory.getBeanDefinition(parentBeanDefinition.getParentName());
                beanClassName = parentBeanDefinition.getBeanClassName();
            }
            return validateStringOnEquality(beanClassName, getAnalyzedBeanClassNames());
        }
    }

    private boolean validateStringOnEquality(String incomingString, String[] equalityCandidates) {
        return Arrays.asList(equalityCandidates).contains(incomingString);
    }
}
