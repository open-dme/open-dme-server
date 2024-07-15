package io.github.opendme.server.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client.id}")
    private String clientId;
    @Value("${keycloak.client.secret}")
    private String clientSecret;


    @Bean
    public Keycloak keycloak() {
        var builder = KeycloakBuilder.builder()
                .serverUrl(System.getenv("KEYCLOAK_URL"))
                .realm(realm)

                .grantType(OAuth2Constants.CLIENT_SECRET)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}
