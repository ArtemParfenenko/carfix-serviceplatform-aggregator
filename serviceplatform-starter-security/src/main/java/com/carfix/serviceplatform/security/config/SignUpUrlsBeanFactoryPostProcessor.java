package com.carfix.serviceplatform.security.config;

import com.carfix.serviceplatform.security.annotation.AllowFor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

public class SignUpUrlsBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private static final String SIGN_UP_URL_PREFIX = "serviceplatform.security.sign-up-urls[";
    private static final String APPLICATION_PROPERTY_SOURCE_NAME = "applicationConfig: [classpath:/application.yaml]";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        ConfigurableEnvironment environment = configurableListableBeanFactory.getBean(ConfigurableEnvironment.class);
        PropertySource<?> propertySource = environment.getPropertySources().get(APPLICATION_PROPERTY_SOURCE_NAME);

        if (propertySource != null && propertySource.getSource() instanceof Map) {
            Map<String, Object> currentSignUpUrls = extractCurrentSignUpUrls(propertySource);

            List<ScannedGenericBeanDefinition> controllers = extractControllers(configurableListableBeanFactory);
            List<String> controllersSignUpUrls = extractControllerSignUpUrls(controllers);

            Map<String, Object> signUpUrls = new HashMap<>(currentSignUpUrls);
            fillSignUpUrlsWithNewValues(signUpUrls, controllersSignUpUrls);

            environment.getPropertySources().addBefore(APPLICATION_PROPERTY_SOURCE_NAME,
                    new OriginTrackedMapPropertySource("sign-up-urls property source", signUpUrls, true));
        }
    }

    private void fillSignUpUrlsWithNewValues(Map<String, Object> currentSignUpUrls, List<String> controllersSignUpUrls) {
        int fromIndex = currentSignUpUrls.size();
        for (int signUpUrlIndex = 0; signUpUrlIndex < controllersSignUpUrls.size(); signUpUrlIndex++) {
            currentSignUpUrls.put(SIGN_UP_URL_PREFIX + (fromIndex + signUpUrlIndex) + "]", controllersSignUpUrls.get(signUpUrlIndex));
        }
    }

    private List<String> extractControllerSignUpUrls(List<ScannedGenericBeanDefinition> controllers) {
        List<String> signUpUrls = new ArrayList<>();
        controllers.forEach(controller -> signUpUrls.addAll(extractControllerSignUpUrls(controller)));
        return signUpUrls;
    }

    private List<String> extractControllerSignUpUrls(ScannedGenericBeanDefinition controller) {
        boolean isSignedUpOverClass = controller.getBeanClass().isAnnotationPresent(AllowFor.class)
                && controller.getBeanClass().getAnnotation(AllowFor.class).signUp();

        String[] pathPrefixes;
        if (controller.getBeanClass().isAnnotationPresent(RequestMapping.class)) {
            pathPrefixes = controller.getBeanClass().getAnnotation(RequestMapping.class).value();
        } else {
            pathPrefixes = new String[]{""};
        }

        return controller.getMetadata().getAnnotatedMethods(RequestMapping.class.getName()).stream()
                .filter(methodMetadata -> (methodMetadata.isAnnotated(AllowFor.class.getName())
                        && methodMetadata.getAnnotationAttributes(AllowFor.class.getName()).containsKey("signUp")
                        && (Boolean) methodMetadata.getAnnotationAttributes(AllowFor.class.getName()).get("signUp"))
                        || isSignedUpOverClass)
                .map(methodMetadata -> (String[]) methodMetadata.getAnnotationAttributes(RequestMapping.class.getName()).get("value"))
                .flatMap(Arrays::stream)
                .flatMap(signUpUrl -> Arrays.stream(pathPrefixes).map(pathPrefix -> pathPrefix + signUpUrl))
                .collect(Collectors.toList());
    }

    private List<ScannedGenericBeanDefinition> extractControllers(ConfigurableListableBeanFactory factory) {
        return Arrays.stream(factory.getBeanDefinitionNames())
                .map(factory::getBeanDefinition)
                .filter(beanDefinition -> beanDefinition instanceof ScannedGenericBeanDefinition)
                .map(beanDefinition -> (ScannedGenericBeanDefinition) beanDefinition)
                .filter(beanDefinition -> {
                    Class<?> beanClass = beanDefinition.getResolvableType().resolve();
                    return beanClass != null &&
                            (beanClass.isAnnotationPresent(RestController.class) || beanClass.isAnnotationPresent(Controller.class));
                })
                .collect(Collectors.toList());
    }

    private Map<String, Object> extractCurrentSignUpUrls(PropertySource<?> propertySource) {
        return ((Map<String, OriginTrackedValue>) propertySource.getSource()).entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(SIGN_UP_URL_PREFIX))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getValue()));
    }
}
