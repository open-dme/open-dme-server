package io.github.opendme.server.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    @Value("{$keycloak.realm}")
    private String realm;
    @Value("{$keycloak.password}")
    private String password;
    @Value("{$keycloak.username}")
    private String username;


    @Bean
    public Keycloak keycloak() {
                return KeycloakBuilder.builder()
                .serverUrl(System.getenv("KEYCLOAK_URL"))
                .realm(realm)
                .clientId("admin-cli")
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .build();
    }
}
