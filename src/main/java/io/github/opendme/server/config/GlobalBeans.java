package io.github.opendme.server.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalBeans {

    @Bean
    public Keycloak keycloak(KeycloakConfig config) {
        return KeycloakBuilder.builder()
                              .grantType(config.getGrantType())
                              .clientId(config.getClientId())
                              //.clientSecret(config.getClientSecret())
                              .username(config.getUsername())
                              .password(config.getPassword())
                              .realm(config.getMasterRealm())
                              .serverUrl(config.getBaseUrl())
                              .build();
    }
}
