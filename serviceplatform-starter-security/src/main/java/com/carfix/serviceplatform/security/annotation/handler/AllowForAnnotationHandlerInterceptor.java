package com.carfix.serviceplatform.security.annotation.handler;

import com.carfix.serviceplatform.core.exception.ServicePlatformException;
import com.carfix.serviceplatform.security.annotation.AllowFor;
import com.carfix.serviceplatform.security.user.ServicePlatformUserRole;
import com.carfix.serviceplatform.security.util.ServicePlatformContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllowForAnnotationHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServicePlatformException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        Method beanMethod = ((HandlerMethod) handler).getMethod();
        Class<?> beanClass = ((HandlerMethod) handler).getBeanType();

        if (!isControllerEndpoint(beanClass)) {
            return false;
        }
        if (!hasNecessaryAnnotation(beanClass, beanMethod)) {
            return false;
        }
        if (isSignUp(beanClass, beanMethod)) {
            return true;
        }

        List<String> currentMethodUserRoles = currentMethodUserRoles(beanClass, beanMethod);
        String currentUserRole = ServicePlatformContext.getCurrentUserRole();

        if (currentMethodUserRoles.contains(currentUserRole)) {
            return true;
        } else {
            throw new ServicePlatformException();
        }
    }

    private boolean isControllerEndpoint(Class<?> beanClass) {
        return beanClass.isAnnotationPresent(RestController.class) || beanClass.isAnnotationPresent(Controller.class);
    }

    private boolean hasNecessaryAnnotation(Class<?> beanClass, Method beanMethod) {
        return beanClass.isAnnotationPresent(AllowFor.class) || beanMethod.isAnnotationPresent(AllowFor.class);
    }

    private boolean isSignUp(Class<?> beanClass, Method beanMethod) {
        if (beanMethod.isAnnotationPresent(AllowFor.class)) {
            return beanMethod.getAnnotation(AllowFor.class).signUp();
        }
        return beanClass.getAnnotation(AllowFor.class).signUp();
    }

    private List<String> currentMethodUserRoles(Class<?> beanClass, Method method) {
        List<String> currentMethodUserRoles = new ArrayList<>();
        currentMethodUserRoles.add(ServicePlatformUserRole.ADMIN);

        if (method.isAnnotationPresent(AllowFor.class)) {
            currentMethodUserRoles.addAll(Arrays.asList(
                    method.getAnnotation(AllowFor.class).users()
            ));
        } else if (beanClass.isAnnotationPresent(AllowFor.class)) {
            currentMethodUserRoles.addAll(Arrays.asList(
                    beanClass.getAnnotation(AllowFor.class).users()
            ));
        }

        return currentMethodUserRoles;
    }
}
