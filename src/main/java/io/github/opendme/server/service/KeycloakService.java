package io.github.opendme.server.service;

import io.github.opendme.server.config.KeycloakConfig;
import io.github.opendme.server.service.keycloak.KeycloakInitializer;
import jakarta.annotation.PostConstruct;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {
    private KeycloakConfig config;
    private Keycloak keycloak;
    private final KeycloakInitializer keycloakInitializer;

    public KeycloakService(KeycloakInitializer keycloakInitializer, Keycloak keycloak, KeycloakConfig config) {
        this.keycloakInitializer = keycloakInitializer;
        this.keycloak = keycloak;
        this.config = config;
    }

    @PostConstruct
    public void init() {
        if (config.isInitializeOnStartup()) {
            keycloakInitializer.init(false);
        }
    }

    public List<UserRepresentation> getInstanceAdmins() {
        List<UserRepresentation> admin = realm()
                .groups()
                .group("admin")
                .members();
        return Collections.emptyList();
    }

    public void createGroup(String group) {
        GroupRepresentation rep = new GroupRepresentation();
        rep.setName("admin");
        var add = realm()
                .groups()
                .add(rep);
    }

    private RealmResource realm() {
        return keycloak.realm(config.getRealm());
    }
}
