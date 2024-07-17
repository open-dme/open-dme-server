package io.github.opendme.server.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalBeans {
    @Autowired
    private KeycloakConfig keycloakConfig;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                              .authorization(OAuth2Constants.CLIENT_SECRET)
                              .clientId(keycloakConfig.getClientId())
                              .clientSecret(keycloakConfig.getClientSecret())
                              .realm(keycloakConfig.getMasterRealm())
                              .serverUrl(keycloakConfig.getBaseUrl())
                              .build();
    }
}
