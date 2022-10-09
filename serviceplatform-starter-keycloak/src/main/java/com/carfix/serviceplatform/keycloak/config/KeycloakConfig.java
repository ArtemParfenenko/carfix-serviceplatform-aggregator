package com.carfix.serviceplatform.keycloak.config;

import com.carfix.serviceplatform.core.QualifierConstants;
import com.carfix.serviceplatform.keycloak.client.KeycloakClient;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class KeycloakConfig {

    @Bean
    @Qualifier(QualifierConstants.KEYCLOAK_ADMIN_CLIENT)
    @ConditionalOnProperty("serviceplatform.security.admin-secret")
    public Keycloak keycloakAdminClient(KeycloakSpringBootProperties keycloakProperties,
                                        SecurityProperties securityProperties) {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getAuthServerUrl())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm("master")
                .clientId("admin-cli")
                .clientSecret(securityProperties.getAdminSecret())
                .build();
    }

    @Bean
    @Primary
    @Qualifier(QualifierConstants.AUTHORIZED_REST_TEMPLATE)
    @ConditionalOnProperty("serviceplatform.security.client-secret")
    public RestTemplate authorizedRestTemplate(RestTemplateBuilder restTemplateBuilder,
                                               KeycloakSpringBootProperties keycloakProperties,
                                               SecurityProperties securityProperties) {
        return restTemplateBuilder
                .additionalInterceptors((request, body, execution) -> {
                    request.getHeaders().add("Authorization", "Bearer " +
                            getAccessToken(keycloakProperties, securityProperties));
                    return execution.execute(request, body);
                })
                .build();
    }

    private String getAccessToken(KeycloakSpringBootProperties keycloakProperties, SecurityProperties securityProperties) {
        String accessTokenString = "";
        try (Keycloak clientKeycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getAuthServerUrl())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakProperties.getResource())
                .clientSecret(securityProperties.getClientSecret())
                .build()) {
            accessTokenString = clientKeycloak.tokenManager().getAccessTokenString();
        }
        return accessTokenString;
    }


    @Bean
    @ConditionalOnBean(name = QualifierConstants.KEYCLOAK_ADMIN_CLIENT)
    public KeycloakClient keycloakClient(@Qualifier(QualifierConstants.KEYCLOAK_ADMIN_CLIENT) Keycloak keycloak,
                                         KeycloakSpringBootProperties keycloakSpringBootProperties) {
        return new KeycloakClient(keycloak, keycloakSpringBootProperties);
    }

    @Bean
    @ConditionalOnProperty("spring.security.oauth2.client.provider.oidc.issuer-uri")
    public KeycloakSpringBootConfigResolverBeanPostProcessor keycloakSpringBootConfigResolverBeanPostProcessor(
            @Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}") String issuerUri
    ) {
        return new KeycloakSpringBootConfigResolverBeanPostProcessor(issuerUri);
    }
}
