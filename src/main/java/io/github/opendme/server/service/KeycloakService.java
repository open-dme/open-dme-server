package io.github.opendme.server.service;

import io.github.opendme.server.config.KeycloakConfig;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {
    @Autowired
    private KeycloakConfig config;

    @Autowired
    private Keycloak keycloak;

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
        Response add = realm()
                .groups()
                .add(rep);
    }

    private RealmResource realm() {
        return keycloak.realm(config.getRealm());
    }
}
