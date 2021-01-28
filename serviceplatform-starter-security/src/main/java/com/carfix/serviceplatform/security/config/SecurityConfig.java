package com.carfix.serviceplatform.security.config;

import com.carfix.serviceplatform.security.annotation.handler.AllowForAnnotationHandlerInterceptor;
import com.carfix.serviceplatform.security.listener.SecurityPropertiesHolderListener;
import com.carfix.serviceplatform.security.util.JwtCreator;
import com.carfix.serviceplatform.security.util.RoleSwitcher;
import com.carfix.serviceplatform.security.util.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@Order(98)
public class SecurityConfig {

    @Bean
    @Order(98)
    public AllowForAnnotationHandlerInterceptor allowForAnnotationHandlerInterceptor() {
        return new AllowForAnnotationHandlerInterceptor();
    }

    @Bean
    @Order(98)
    public ServicePlatformInterceptorConfigurer platformWebConfigurerAdapter(AllowForAnnotationHandlerInterceptor allowForAnnotationHandlerInterceptor) {
        return new ServicePlatformInterceptorConfigurer(allowForAnnotationHandlerInterceptor);
    }

    @Bean
    @Order(98)
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1000)
    @ConditionalOnSecurityProperties
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    @Order(99)
    @ConditionalOnSecurityProperties
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder,
                                                                     SecurityProperties securityProperties) {
        return new ServicePlatformSecurityConfigurerAdapter(userDetailsService, bCryptPasswordEncoder, securityProperties);
    }

    @Bean
    public SecurityPropertiesHolderListener securityPropertiesHolderListener(SecurityProperties securityProperties) {
        return new SecurityPropertiesHolderListener(securityProperties, JwtCreator.getInstance(), RoleSwitcher.getInstance());
    }

    @Bean
    public static SignUpUrlsBeanFactoryPostProcessor signUpUrlsBeanFactoryPostProcessor() {
        return new SignUpUrlsBeanFactoryPostProcessor();
    }
}
